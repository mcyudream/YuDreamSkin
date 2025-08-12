package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.Closet
import online.yudream.yudreamskin.entity.GameProfile
import online.yudream.yudreamskin.entity.Skin
import online.yudream.yudreamskin.entity.User
import online.yudream.yudreamskin.mapper.ClosetMapper
import online.yudream.yudreamskin.mapper.GameProfileMapper
import online.yudream.yudreamskin.mapper.SkinMapper
import online.yudream.yudreamskin.service.SkinService
import online.yudream.yudreamskin.utils.MinioUtils
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class SkinServiceImpl : SkinService {
    @Autowired
    private lateinit var gameProfileMapper: GameProfileMapper

    @Resource
    lateinit var skinMapper: SkinMapper
    @Resource
    lateinit var minioUtils: MinioUtils
    @Resource
    lateinit var closetMapper: ClosetMapper

    override fun uploadSkin(session: HttpSession, skinFile: MultipartFile, name: String, status: Int, skinType: String) : R<Skin?> {
        val user = session.getAttribute("user") as User?
        if (user == null) {
            return R.fail<Skin?>("无效会话!")
        }
        val file = minioUtils.uploadFile(skinFile)
        val metadata = mutableMapOf<String, String>()
        var type = "skin"
        if (skinType == "cape"){
            type = "cape"
        } else{
            metadata.put("model", skinType)
        }
        val skin = Skin(name, file, metadata,status,0,type)
        skin.hash = DigestUtils.sha256Hex(skinFile.bytes)
        val skinEntity = skinMapper.save(skin)
        val closetEntity = Closet()
        closetEntity.skin = skinEntity
        closetEntity.user = user
        closetMapper.save(closetEntity)
        return R.ok<Skin>(skinEntity)
    }

    override fun getUserSkinPage(session: HttpSession, page: Int, size: Int, type: String) : R<Page<Closet>> {
        val user = session.getAttribute("user") as User?
        if (user == null) {
            return R.fail("无效会话")
        }

            val res = closetMapper.findClosetsByUser(user, PageRequest.of(page-1, size))
            return R.ok(res)

    }

    override fun  findClosetById( id: String): Closet? {
        return closetMapper.findById(id).orElse(null)
    }

    override fun setTextures(session: HttpSession, profileId: String, textureId: String) : R<GameProfile> {
        val user = session.getAttribute("user") as User?
        if (user == null) {
            return R.fail("无效会话!")
        }
        var profile = gameProfileMapper.findGameProfileByUserAndUuid(user, profileId)
        if (profile == null) {
            return R.fail("不存在的角色")
        }
        else {
            val skin = skinMapper.findById(textureId).orElse(null)
            if (skin == null) {
                return R.fail("不存在的材质")
            } else {
                if (skin.skinType=="cape"){
                    profile.cape = skin
                } else if (skin.skinType=="skin"){
                    profile.skin = skin
                }
                profile = gameProfileMapper.save(profile)
                return R.ok(profile)
            }
        }
    }
}