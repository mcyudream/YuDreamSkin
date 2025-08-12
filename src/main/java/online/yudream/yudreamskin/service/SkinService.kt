package online.yudream.yudreamskin.service

import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.Closet
import online.yudream.yudreamskin.entity.GameProfile
import online.yudream.yudreamskin.entity.Skin
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile

interface SkinService {
    fun uploadSkin(session: HttpSession, skinFile: MultipartFile, name: String, status: Int, skinType: String): R<Skin?>
    fun findClosetById(id: String): Closet?
    fun getUserSkinPage(session: HttpSession, page: Int, size: Int, type: String): R<Page<Closet>>
    fun setTextures(session: HttpSession, profileId: String, textureId: String): R<GameProfile>
}