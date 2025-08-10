package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.common.enums.SystemRole;
import online.yudream.yudreamskin.entity.Role;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.mapper.RoleMapper;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.UserService;
import online.yudream.yudreamskin.utils.MailUtils;
import online.yudream.yudreamskin.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service

public class UserServiceImpl implements UserService {
    @Value("${default-user.password}")
    private String defaultUserPassword;
    @Value("${default-user.username}")
    private String defaultUserUsername;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MinioUtils minioUtils;
    @Autowired
    private MailUtils mailUtils;

    @Override
    public void createDefaultUser() {
        User user = userMapper.findUserByUsername(defaultUserUsername);
        if (user == null) {
            log.info("Creating default user: {}, {}", defaultUserUsername, defaultUserPassword);
            user = new User();
            Role role = roleMapper.findById(Objects.requireNonNull(SystemRole.SUPER_ADMIN.getRole().getId())).orElse(null);
            if (role == null) {
                throw new RuntimeException("不存在的角色");
            }
            user.setRoles(List.of(role));
            user.setUsername(defaultUserUsername);
            user.setPassword(passwordEncoder.encode(defaultUserPassword));
            userMapper.save(user);
        }
    }

    @Override
    public R<User> changeBaseInfo(HttpSession session, String nickname, MultipartFile avatar){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return R.fail("无效会话!");
        }
        user.setNickname(nickname);
        if (avatar != null) {
            if (avatar.getOriginalFilename() != null && !avatar.getOriginalFilename().isEmpty()) {
                String avatarFile = minioUtils.uploadFile(avatar);
                user.setAvatar(avatarFile);

            }

        }
        user = userMapper.save(user);
        session.setAttribute("user", user);
        return R.ok(user);
    }

    @Override
    public R<User> changeContact(HttpSession session, String email, String emailCode, String qq){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return R.fail("无效会话!");
        }
        if (mailUtils.viaCaptcha(email,emailCode, "change")){
            user.setEmail(email);
            user.setQq(qq);
            user = userMapper.save(user);
        } else {
            return R.fail("邮箱验证失败");
        }
        return R.ok(user);
    }

    @Override
    public R<User> changePassword(HttpSession session, String rawPassword, String newPassword){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return R.fail("无效会话!");
        }
        if  (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return R.fail("原密码错误");
        } else {
            if (passwordVia(newPassword)){
                user.setPassword(passwordEncoder.encode(newPassword));
                user = userMapper.save(user);
                return R.ok(user);
            }
            else {
                return R.fail("密码校验失败");
            }
        }
    }

    private boolean passwordVia(String password) {
        // 非空 & 长度 ≥ 8 & 同时包含字母和数字
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Za-z].*")   // 至少一个字母
                && password.matches(".*[0-9].*");     // 至少一个数字
    }
}
