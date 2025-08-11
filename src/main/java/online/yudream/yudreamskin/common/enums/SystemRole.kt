package online.yudream.yudreamskin.common.enums

import online.yudream.yudreamskin.entity.Role

enum class SystemRole (val role: Role) {
    SUPER_ADMIN(Role("super_admin", "超级管理员", "超级管理员", "admin")),
    ADMIN(Role("admin","管理员","管理员",  "admin")),
    USER(Role("user", "用户", "用户"))
}
