package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    @Override
    public void createDefaultUser() {
        User user = userMapper.findUserByUsername(defaultUserUsername);
        if (user == null) {
            log.info("Creating default user: {}, {}", defaultUserUsername, defaultUserPassword);
            user = new User();
            user.setUsername(defaultUserUsername);
            user.setPassword(passwordEncoder.encode(defaultUserPassword));
            userMapper.save(user);
        }
    }

    @Override
    public R<User> login(String username, String password, HttpSession session) {
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            return R.fail(403, "不存在的用户");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return R.fail(403, "密码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return R.ok(user);
    }
}
