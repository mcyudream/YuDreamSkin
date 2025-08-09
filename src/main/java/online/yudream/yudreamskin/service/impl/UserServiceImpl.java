package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import online.yudream.yudreamskin.common.enums.SystemRole;
import online.yudream.yudreamskin.entity.Role;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.mapper.RoleMapper;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public void createDefaultUser() {
        User user = userMapper.findUserByUsername(defaultUserUsername);
        if (user == null) {
            log.info("Creating default user: {}, {}", defaultUserUsername, defaultUserPassword);
            user = new User();
            Role role = roleMapper.findRoleByName(Objects.requireNonNull(SystemRole.SUPER_ADMIN.getRole().getName()));
            user.setRoles(List.of(role));
            user.setUsername(defaultUserUsername);
            user.setPassword(passwordEncoder.encode(defaultUserPassword));
            userMapper.save(user);
        }
    }
}
