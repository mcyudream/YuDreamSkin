package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.GameProfile
import online.yudream.yudreamskin.entity.User
import online.yudream.yudreamskin.mapper.GameProfileMapper
import online.yudream.yudreamskin.mapper.UserMapper
import online.yudream.yudreamskin.service.GameProfileService
import online.yudream.yudreamskin.utils.UUIDUtils
import org.springframework.stereotype.Service

@Service
class GameProfileServiceImpl : GameProfileService {

    @Resource
    lateinit var userMapper: UserMapper
    @Resource
    lateinit var uuidUtils: UUIDUtils
    @Resource
    lateinit var gameProfileMapper: GameProfileMapper

    override fun createProfile(name: String, session: HttpSession): R<GameProfile> {
        val user = session.getAttribute("user") as User?
        if (user == null) {
            return R.fail("无效会话!")
        }
        var profile : GameProfile? = gameProfileMapper.findById(uuidUtils.generateNoSymbolUUID(name)).orElse(null)
        if (profile != null) {
            return R.fail("角色名重复，请重新尝试!")
        }
        profile = GameProfile()
        profile.name = name
        profile.uuid = uuidUtils.generateNoSymbolUUID(name)
        profile.user = user
        profile = gameProfileMapper.save(profile)
        return R.ok(profile)
    }

    override fun getUserProfile(user: User, search: String?): List<GameProfile> {
        if (search == null || search.isEmpty()) {
            println(gameProfileMapper.findGameProfileByUser(user))
            return gameProfileMapper.findGameProfileByUser(user)
        } else{
            return gameProfileMapper.findGameProfileByUserAndNameLike(user, "%${search}%")
        }
    }
}