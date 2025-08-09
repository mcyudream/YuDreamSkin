package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import online.yudream.yudreamskin.common.enums.SystemRole
import online.yudream.yudreamskin.mapper.RoleMapper
import online.yudream.yudreamskin.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl : RoleService{
    @Resource
    lateinit var roleMapper: RoleMapper

    override fun initSystemRoles(){
        roleMapper.deleteAll()
        SystemRole.entries.forEach { role -> roleMapper.save(role.role)}
    }
}
