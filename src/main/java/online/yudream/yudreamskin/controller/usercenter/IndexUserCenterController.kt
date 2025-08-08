package online.yudream.yudreamskin.controller.usercenter

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class IndexUserCenterController {

    @GetMapping
    fun index(model: Model, session: HttpSession):String {
        model.addAttribute("title", "个人主页")
        return "view/user-center/index"
    }
}