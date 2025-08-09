package online.yudream.yudreamskin.service

import online.yudream.yudreamskin.entity.Menu

interface MenuService {
    fun getMenu(type: String): Menu
    fun initSystemMenu()
}