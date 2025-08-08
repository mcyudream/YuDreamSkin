package online.yudream.yudreamskin.mapper

import online.yudream.yudreamskin.entity.MenuItem
import org.springframework.data.mongodb.repository.MongoRepository

interface MenuMapper: MongoRepository<MenuItem, String> {
}