package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
