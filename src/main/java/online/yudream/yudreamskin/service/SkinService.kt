package online.yudream.yudreamskin.service

import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.Skin
import org.springframework.web.multipart.MultipartFile

interface SkinService {
    fun uploadSkin(session: HttpSession, skinFile: MultipartFile, name: String, status: Int, skinType: String): R<Skin?>
}