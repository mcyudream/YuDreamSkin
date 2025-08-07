package online.yudream.yudreamskin.controller.api;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passkey")
public class PasskeyController {
    @Resource
    private AuthService authService;
    @GetMapping("/register/options")
    public String getPasskeyRegistrationOptions(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "";
        }
        return authService.startPasskeyRegistration(user.getId());
    }

    @PostMapping("/register")
    public String verifyPasskeyRegistration(@RequestBody String credential, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "";
        }
        authService.finishPasskeyRegistration(user.getId(), credential);
        return "";
    }

    @GetMapping("/login/options")
    public String getPasskeyAssertionOptions(HttpSession session) {
        return authService.startPasskeyAssertion(session.getId());
    }

    @PostMapping("/login")
    public String verifyPasskeyAssertion(@RequestBody String credential, HttpSession session) {
        var user = authService.finishPasskeyAssertion(session.getId(), credential);
        return "";
    }
}
