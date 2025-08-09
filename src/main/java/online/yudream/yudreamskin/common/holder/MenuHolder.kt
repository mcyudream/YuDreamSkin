package online.yudream.yudreamskin.common.holder

import jakarta.annotation.PostConstruct
import online.yudream.yudreamskin.entity.Menu
import online.yudream.yudreamskin.service.MenuService
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference

@Component
class MenuHolder(private val menuService: MenuService) {
    private val cacheHomeTop = AtomicReference<Menu>()
    private val cacheUserSide = AtomicReference<Menu>()

    @PostConstruct
    fun load() {
        refresh()
    }

    /** 供外部调用：刷新缓存 */
    fun refresh() {
        cacheHomeTop.set(menuService.getMenu("home_top_nav"))
        cacheUserSide.set(menuService.getMenu("user_side_menu"))
//        cacheHomeTop.set(menuService.getMenu("home_top_nav"))
    }

    fun getHomeTopNav(): Menu = cacheHomeTop.get()
    fun getUserSideMenu(): Menu = cacheUserSide.get()
}
