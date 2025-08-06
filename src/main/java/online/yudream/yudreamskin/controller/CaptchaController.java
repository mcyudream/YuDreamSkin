package online.yudream.yudreamskin.controller;

import jakarta.annotation.Resource;
import online.yudream.yudreamskin.service.CaptchaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerCaptcha(@RequestParam String email){
        captchaService.sendCaptcha(email,"register","注册");
    }
}
