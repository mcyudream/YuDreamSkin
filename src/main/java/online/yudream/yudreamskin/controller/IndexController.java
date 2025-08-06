package online.yudream.yudreamskin.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping
    public String index() {
        return "view/home/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "登录");
        return "view/home/login";
    }
}
