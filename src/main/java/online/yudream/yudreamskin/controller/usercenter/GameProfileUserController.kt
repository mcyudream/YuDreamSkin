package online.yudream.yudreamskin.controller.usercenter

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.service.GameProfileService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/user/gameProfile")
class GameProfileUserController {

    @Resource
    lateinit var gameProfileService: GameProfileService

    @GetMapping
    fun gameProfile(): String{
        return "view/user-center/gameProfile"
    }

    @PostMapping("/create")
    fun create(@RequestParam profileName: String, session: HttpSession):String{
        val res = gameProfileService.createProfile(profileName, session)
        return when(res.code){
            200 -> "redirect:/user/gameProfile?success"
            else -> "redirect:/user/gameProfile?error="+res.msg
        }
    }

}