package online.yudream.yudreamskin.service;

import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;

public interface UserService {
    void createDefaultUser();

    R<User> login(String username, String password);
}
