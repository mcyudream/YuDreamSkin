package online.yudream.yudreamskin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TestPluginController {

    @GetMapping("/plugin/{viewName}")
    public String pluginView(@PathVariable String viewName) {
        return viewName; // 直接返回视图名，Thymeleaf会自动查找
    }
}