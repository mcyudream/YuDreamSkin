package online.yudream.yudreamskin.controller.usercenter

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/user/gameProfile")
class GameProfileUserController {

    @GetMapping
    fun gameProfile(): String{
        return "view/user-center/gameProfile"
    }


}