package online.yudream.yudreamskin.controller.usercenter

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user/gameProfile")
class GameProfileUserController {

    @GetMapping
    fun gameProfile(): String{
        return "view/user-center/gameProfile"
    }
}