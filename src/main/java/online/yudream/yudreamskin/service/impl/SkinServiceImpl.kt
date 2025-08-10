package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import online.yudream.yudreamskin.mapper.SkinMapper
import online.yudream.yudreamskin.service.SkinService
import org.springframework.stereotype.Service

@Service
class SkinServiceImpl : SkinService {
    @Resource
    lateinit var skinMapper: SkinMapper


}