package online.yudream.yudreamskin.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId


@Document("tb_menu")
data class Menu (
    var type: String,
    var menus: List<MenuItem>,
) {
    @MongoId
    var id  = null;
}

data class MenuItem(
    var name: String,
    var svgIcon: String,
    var index: Int,
    var path: String,
    var parent: MenuItem? = null,
) {

}