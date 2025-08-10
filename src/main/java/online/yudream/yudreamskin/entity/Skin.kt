package online.yudream.yudreamskin.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId
import java.io.Serializable
import java.time.LocalDateTime

@Document(collection = "tb_skin")
data class Skin (
    val name: String? = null,
    val fileName: String? = null,
    val metadata: Map<String, String>? =null,
    val status: Int = 0, //0: 公开 1: 私密
    val like: Int = 0,
    val skinType: String = "skin", //或cape
    @MongoId
    val id: String? = null,
): Serializable {
    @DocumentReference(lazy = true)
    val user: User? = null
    @CreatedDate
    var createdAt: LocalDateTime? = null
}