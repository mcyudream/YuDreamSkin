package online.yudream.yudreamskin.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/migrate")
class MigrateAdminController {

    @GetMapping
    fun migrate() :String {
        return "view/admin/migrate"
    }
}