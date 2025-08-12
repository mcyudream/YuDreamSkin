package online.yudream.yudreamskin.controller.usercenter

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.GameProfile
import online.yudream.yudreamskin.entity.User
import online.yudream.yudreamskin.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/user/profile")
class ProfileUserController {
    @Resource
    lateinit var userService: UserService

    @GetMapping
    fun profile(): String {
        return "view/user-center/profile"
    }

    @PostMapping("/changeBase")
    fun changeBaseInfo(session: HttpSession, @RequestParam nickname: String, @RequestParam avatar: MultipartFile): String{
        val res : R<User> = userService.changeBaseInfo(session, nickname, avatar)
        return when (res.code) {
            200 -> "redirect:/user/profile?success"
            else ->  "redirect:/user/profile?error=" + res.msg
        }
    }

    @PostMapping("/changeContact")
    fun changeContact(session: HttpSession, @RequestParam email: String, @RequestParam emailCode: String, @RequestParam qq: String): String {
        val res : R<User> = userService.changeContact(session, email, emailCode, qq)
        return when (res.code) {
            200 -> "redirect:/user/profile?success=true"
            else -> "redirect:/user/profile?error=" + res.msg
        }
    }

    @PostMapping("/changePassword")
    fun changePassword(session: HttpSession, @RequestParam rawPassword: String, @RequestParam newPassword: String): String {
        val res : R<User> = userService.changePassword(session, rawPassword, newPassword)
        return when (res.code) {
            200 -> "redirect:/user/profile?success=true"
            else -> "redirect:/user/profile?error=" + res.msg
        }
    }


}