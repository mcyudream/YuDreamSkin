package online.yudream.yudreamskin.service.impl

import jakarta.annotation.Resource
import online.yudream.yudreamskin.common.enums.SystemMenus
import online.yudream.yudreamskin.entity.Menu
import online.yudream.yudreamskin.mapper.MenuMapper
import online.yudream.yudreamskin.service.MenuService
import org.springframework.stereotype.Service

@Service
class MenuServiceImpl : MenuService {
    @Resource
    lateinit var menuMapper: MenuMapper

    override fun getMenu(type: String): Menu {
        return menuMapper.findMenuByType(type);
    }

    override fun initSystemMenu() {
        menuMapper.deleteAll()
        for (menu: SystemMenus in SystemMenus.entries){
            menuMapper.save(menu.sysMenuToMenu())
        }
    }
}