package online.yudream.yudreamskin.controller.common

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class NotFoundController {

    @GetMapping("/404")
    fun notFound(): String {
        return "view/home/404"
    }
}