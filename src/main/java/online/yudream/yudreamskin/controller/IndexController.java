package online.yudream.yudreamskin.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {
    @Resource
    private AuthService authService;

    @GetMapping
    public String index() {
        return "view/home/index";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "登录");
        return "view/home/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        R<User> res = authService.login(username, password, session);
        if (res.isSuccess()) {
            User user = res.getData();
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            session.removeAttribute("user");
            return  "redirect:/login?error="+res.getMsg();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        SecurityContextHolder.clearContext();
        session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        model.addAttribute("title", "注册");
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        } else {
            return "view/home/register";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String emailCode,
                           HttpSession session) {
        R<User> res = authService.register(username, password, email, emailCode);
        if (res.isSuccess()) {
            return "redirect:/login";

        }else {
            return "redirect:/register?error="+res.getMsg();

        }
    }

    @GetMapping("/forget")
    public String forget(Model model, HttpSession session) {
        model.addAttribute("title", "找回密码");
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        } else {
            return "view/home/forget";
        }
    }

    @PostMapping("/forget")
    public String forget(@RequestParam String email,@RequestParam String emailCode, @RequestParam String password, HttpSession session) {
        R<User> res = authService.forgetPassword(email, password, emailCode);
        if  (res.isSuccess()) {
            return "redirect:/login";
        } else {
            return "redirect:/forget?error="+res.getMsg();
        }
    }


}
