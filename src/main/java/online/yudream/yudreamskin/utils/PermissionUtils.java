package online.yudream.yudreamskin.utils;

import online.yudream.yudreamskin.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PermissionUtils {

    public boolean checkPermissionLevel(String level, List<Role> roles) {
        for (Role role : roles) {
            if (Objects.equals(role.getLevel(), level)) {
                return true;
            }
        }
        return false;
    }
}
