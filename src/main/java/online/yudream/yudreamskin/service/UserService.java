package online.yudream.yudreamskin.service;

import jakarta.servlet.http.HttpSession;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void createDefaultUser();

    R<User> changeBaseInfo(HttpSession session, String nickname, MultipartFile avatar);

    R<User> changeContact(HttpSession session, String email, String emailCode, String qq);

    R<User> changePassword(HttpSession session, String rawPassword, String newPassword);
}
