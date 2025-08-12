package online.yudream.yudreamskin.service.impl;

import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.common.enums.SystemRole;
import online.yudream.yudreamskin.entity.IpEntity;
import online.yudream.yudreamskin.entity.Role;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.entity.WebauthnCredential;
import online.yudream.yudreamskin.mapper.RoleMapper;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.mapper.WebauthnCredentialMapper;
import online.yudream.yudreamskin.service.AuthService;
import online.yudream.yudreamskin.utils.ByteUtil;
import online.yudream.yudreamskin.utils.MailUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final String REDIS_PASSKEY_REGISTRATION_KEY = "passkey:registration";
    private final String REDIS_PASSKEY_ASSERTION_KEY = "passkey:assertion";
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MailUtils mailUtils;
    @Resource
    private StringRedisTemplate template;
    @Resource
    private RelyingParty relyingParty;
    @Resource
    private WebauthnCredentialMapper webauthnCredentialMapper;
    @Resource
    private RoleMapper roleMapper;
    @Override
    public R<User> login(String username, String password, HttpSession session, HttpServletRequest request) {
        User user = userMapper.findUserByUsernameOrEmail(username,username);
        if (user == null) {
            return R.fail(403, "不存在的用户");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return R.fail(403, "密码错误");
        }
        String ip = request.getRemoteAddr();
        IpEntity ipEntity = new IpEntity(ip, LocalDateTime.now());
        if (user.getLoginIps().size()>5){
            user.getLoginIps().remove(0);
        }
        user.getLoginIps().add(ipEntity);
        userMapper.save(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return R.ok(user);
    }

    @Override
    public R<User> register(String username, String password, String email, String emailCode) {
        User rawUser = userMapper.findUserByUsernameOrEmail(username, email);
        if (rawUser == null) {
            if (mailUtils.viaCaptcha(email,emailCode, "register")){
                if (!passwordVia(password)){
                    return R.fail("密码校验失败!");
                }
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setEmail(email);
                user.setNickname(username);
                Role role = roleMapper.findById(Objects.requireNonNull(SystemRole.USER.getRole().getId())).orElse(null);
                if (role == null) {
                    throw new RuntimeException("不存在的角色!");
                }
                user.setRoles(List.of(role));
                user = userMapper.save(user);

                return R.ok(user);
            } else {
                return R.fail(400, "验证码错误!");
            }

        } else {
            return R.fail(400, "用户已存在!");
        }
    }

    @Override
    public R<User> forgetPassword(String email, String password, String emailCode) {
        User user = userMapper.findUserByEmail(email);
        if (user == null) {
            return R.fail(400, "用户不存在!");
        } else {
            if (mailUtils.viaCaptcha(email,emailCode, "forget")){
                if (!passwordVia(password)){
                    return R.fail("密码校验失败!");
                }
                user.setPassword(passwordEncoder.encode(password));
                user = userMapper.save(user);
                return R.ok(user);
            }
            return R.fail(400, "验证码错误!");
        }
    }

    // todo 错误处理
    @SneakyThrows
    @Override
    public String startPasskeyRegistration(String userID) {
        var user = userMapper.findById(userID);

        if (user.isEmpty()){
            throw new RuntimeException();
        }

        var options = relyingParty.startRegistration(StartRegistrationOptions.builder()
                .user(UserIdentity.builder()
                        .name(user.get().getEmail())
                        .displayName(user.get().getUsername())
                        .id(new ByteArray(ByteUtil.getBytes(user.get().getId())))
                        .build())
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                        .residentKey(ResidentKeyRequirement.REQUIRED)
                        .build())
                .build());
        template.opsForHash().put(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.get().getId()), options.toJson());
        return options.toCredentialsCreateJson();
    }

    @SneakyThrows
    @Override
    public void finishPasskeyRegistration(String userID, String credential) {
        var user = userMapper.findById(userID);

        if (user.isEmpty()){
            throw new RuntimeException();
        }

        String decoded = URLDecoder.decode(credential, StandardCharsets.UTF_8);
        var pkc = PublicKeyCredential.parseRegistrationResponseJson(decoded);
        var request = PublicKeyCredentialCreationOptions.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.get().getId())));
        var result = relyingParty.finishRegistration(FinishRegistrationOptions.builder()
                .request(request)
                .response(pkc)
                .build());
        template.opsForHash().delete(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.get().getId()));
        webauthnCredentialMapper.save(WebauthnCredential.from(userID, request, result));
    }

    @SneakyThrows
    @Override
    public String startPasskeyAssertion(String identifier) {
        var options = relyingParty.startAssertion(StartAssertionOptions.builder().build());
        template.opsForHash().put(REDIS_PASSKEY_ASSERTION_KEY, identifier, options.toJson());
        return options.toCredentialsGetJson();
    }

    @SneakyThrows
    @Override
    public User finishPasskeyAssertion(String identifier, String credential) {
        var request = AssertionRequest.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_ASSERTION_KEY, identifier));
        String decoded = URLDecoder.decode(credential, StandardCharsets.UTF_8);
        var pkc = PublicKeyCredential.parseAssertionResponseJson(decoded);
        var result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
                .request(request)
                .response(pkc)
                .build());
        template.opsForHash().delete(REDIS_PASSKEY_ASSERTION_KEY, identifier);
        if (!result.isSuccess()) {
            throw new AssertionFailedException("Verify failed");
        }
        var user = userMapper.findUserByEmail(result.getUsername());
        var entity = webauthnCredentialMapper.findAllByUserID(user.getId())
                .stream()
                .filter(it -> result.getCredential().getCredentialId().equals(it.getCredentialRegistration().getCredential().getCredentialId()))
                .findAny()
                .orElseThrow();
        entity.getCredentialRegistration().setCredential(entity.getCredentialRegistration().getCredential().toBuilder().signatureCount(result.getSignatureCount()).build());
        webauthnCredentialMapper.save(entity);
        return user;
    }

    private boolean passwordVia(String password) {
        // 非空 & 长度 ≥ 8 & 同时包含字母和数字
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Za-z].*")   // 至少一个字母
                && password.matches(".*[0-9].*");     // 至少一个数字
    }
}
