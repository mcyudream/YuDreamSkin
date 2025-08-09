package online.yudream.yudreamskin.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.io.Serializable

@Document(collection = "tb_permission")
data class Permission (
    @MongoId
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val displayName: String? = null,
) : Serializable {

}