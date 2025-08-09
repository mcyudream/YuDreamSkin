package online.yudream.yudreamskin.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;

public interface AuthService {


    R<User> login(String username, String password, HttpSession session, HttpServletRequest request);

    R<User> register(String username, String password, String email, String emailCode);

    R<User> forgetPassword(String email, String password, String emailCode);

    String startPasskeyRegistration(String userID);

    void finishPasskeyRegistration(String userID, String credential);

    String startPasskeyAssertion(String identifier);

    User finishPasskeyAssertion(String identifier, String credential);
}
