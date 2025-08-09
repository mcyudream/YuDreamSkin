package online.yudream.yudreamskin.mapper

import online.yudream.yudreamskin.entity.Menu
import org.springframework.data.mongodb.repository.MongoRepository

interface MenuMapper: MongoRepository<Menu, String> {
    fun findMenuByType(type: String): Menu
}