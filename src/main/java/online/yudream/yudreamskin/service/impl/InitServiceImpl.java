package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import online.yudream.yudreamskin.service.InitService;
import online.yudream.yudreamskin.service.MenuService;
import online.yudream.yudreamskin.service.RoleService;
import online.yudream.yudreamskin.service.UserService;
import online.yudream.yudreamskin.utils.MinioUtils;
import org.springframework.stereotype.Service;

@Service
public class InitServiceImpl implements InitService {
    @Resource
    private MinioUtils minioUtils;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;

    @Override
    public void initApp(){
        minioUtils.initBucket();
        roleService.initSystemRoles();
        userService.createDefaultUser();
        menuService.initSystemMenu();
    }
}
