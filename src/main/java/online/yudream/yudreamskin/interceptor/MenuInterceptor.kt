package online.yudream.yudreamskin.interceptor

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import online.yudream.yudreamskin.common.enums.SystemMenus
import online.yudream.yudreamskin.service.MenuService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class MenuInterceptor : HandlerInterceptor {
    @Resource
    lateinit var menuService: MenuService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val path = request.requestURI
        val session = request.session
        addMenu(session, SystemMenus.HOME_TOP_NAV)
        return super.preHandle(request, response, handler)

    }

    private fun addMenu(session: HttpSession, menu: SystemMenus){
        if (session.getAttribute(menu.menuName)==null){
            session.setAttribute(menu.menuName, menuService.getMenu(menu.menu.type))
        }
    }
}
