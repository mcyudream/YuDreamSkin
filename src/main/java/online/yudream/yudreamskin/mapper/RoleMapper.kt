package online.yudream.yudreamskin.mapper

import online.yudream.yudreamskin.entity.Role
import org.springframework.data.mongodb.repository.MongoRepository

interface RoleMapper : MongoRepository<Role, String> {
    fun findRoleByName(name: String): Role
}
