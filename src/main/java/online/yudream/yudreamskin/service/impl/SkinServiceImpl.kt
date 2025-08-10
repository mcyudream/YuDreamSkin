package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.R
import online.yudream.yudreamskin.entity.Closet
import online.yudream.yudreamskin.entity.Skin
import online.yudream.yudreamskin.entity.User
import online.yudream.yudreamskin.mapper.ClosetMapper
import online.yudream.yudreamskin.mapper.SkinMapper
import online.yudream.yudreamskin.service.SkinService
import online.yudream.yudreamskin.utils.MinioUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class SkinServiceImpl : SkinService {
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
        val skinEntity = skinMapper.save(skin)
         val closetEntity = Closet()

        closetEntity.skin = skinEntity
        closetEntity.user = user
        closetMapper.save(closetEntity)
        return R.ok<Skin>(skinEntity)
    }

    override fun getUserSkinPage(session: HttpSession, page: Int, size: Int) : R<Page<Closet>> {
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
}