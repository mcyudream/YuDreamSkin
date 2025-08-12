package online.yudream.yudreamskin.service

import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.GameProfile
import online.yudream.yudreamskin.entity.User

interface GameProfileService {
    fun createProfile(name: String, session: HttpSession): R<GameProfile>
    fun getUserProfile(user: User, search: String?): List<GameProfile>
    fun deleteProfile(session: HttpSession, profileId: String): R<GameProfile>
}