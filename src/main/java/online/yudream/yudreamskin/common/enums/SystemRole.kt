package online.yudream.yudreamskin.common.enums

import online.yudream.yudreamskin.entity.Role

enum class SystemRole (val role: Role) {
    SUPER_ADMIN(Role(id=null,"super_admin", "超级管理员", "超级管理员", level = "admin")),
    ADMIN(Role(id=null,"admin","管理员","管理员", level = "admin")),
    USER(Role(id=null,"user", "用户", "用户"))
}
