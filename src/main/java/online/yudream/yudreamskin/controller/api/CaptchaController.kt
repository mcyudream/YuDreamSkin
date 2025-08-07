package online.yudream.yudreamskin.controller.api

import jakarta.annotation.Resource
import online.yudream.yudreamskin.service.CaptchaService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/captcha")
class CaptchaController {
    @Resource
    private lateinit var captchaService: CaptchaService

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerCaptcha(@RequestParam email: String?) {
        captchaService.sendCaptcha(email, "register", "注册")
    }

    @GetMapping("/forget")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun forgetCaptcha(@RequestParam email: String?) {
        captchaService.sendCaptcha(email, "forget", "找回密码")
    }
}
