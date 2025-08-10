package online.yudream.yudreamskin.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId
import java.io.Serializable

@Document(collection = "tb_role")
data class Role  (
    @MongoId
    val id: String ?= null,
    val description: String? = null,
    val displayName: String? = null,
    val level: String? = null
) : Serializable {
    @DocumentReference(lazy = true) val permission: List<Permission>? = null
}