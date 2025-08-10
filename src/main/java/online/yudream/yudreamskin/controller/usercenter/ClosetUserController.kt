package online.yudream.yudreamskin.controller.usercenter

import jakarta.annotation.Resource
import jakarta.mail.Session
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.service.SkinService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/user/closet")
class ClosetUserController  {
    @Resource
    lateinit var skinService: SkinService

    @GetMapping
    fun closet():String {
        return "/view/user-center/closet"
    }

    @PostMapping("/upload")
    fun uploadSkin(session: HttpSession,
                   @RequestParam name: String,
                   @RequestParam skinFile: MultipartFile,
                   @RequestParam skinType: String,
                   @RequestParam status: Int,
                   ): String{
        val res = skinService.uploadSkin(session,skinFile,name, status, skinType)
        return when (res.code){
            200 -> "redirect:/user/closet?success"
            else -> "redirect:/user/closet?error=" + res.msg
        }
    }
}