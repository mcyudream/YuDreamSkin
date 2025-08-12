package online.yudream.yudreamskin.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class IndexAdminController {

    @GetMapping
    fun index(): String {
        return "view/admin/index"
    }
}