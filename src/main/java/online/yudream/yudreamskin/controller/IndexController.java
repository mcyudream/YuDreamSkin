package online.yudream.yudreamskin.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import online.yudream.yudreamskin.common.R;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpResponse;

@Controller
public class IndexController {
    @Resource
    private UserService userService;

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
        R<User> res = userService.login(username, password, session);
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
        return "redirect:/";
    }
}
