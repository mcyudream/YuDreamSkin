package online.yudream.yudreamskin.entity

import org.simpleframework.xml.Serializer
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.io.Serializable


@Document("tb_menu")

data class Menu(

    var type: String = "home-top-nav",
    var menus: List<MenuItem>? = null
) : Serializable {
    @MongoId
    var id: String? = null
}

data class MenuItem(
    var name: String? = null,
    var svgIcon: String? = null,
    var index: Int,
    var path: String = "/",
    var child: List<MenuItem>? = null,
) : Serializable