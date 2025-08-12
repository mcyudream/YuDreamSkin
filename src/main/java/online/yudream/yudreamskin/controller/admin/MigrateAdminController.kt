package online.yudream.yudreamskin.controller.admin

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletResponse
import okhttp3.Response
import online.yudream.yudreamskin.entity.dto.MysqlConnDTO
import online.yudream.yudreamskin.service.MigrationService
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets


@Controller
@RequestMapping("/admin/migrate")
class MigrateAdminController {

    @GetMapping
    fun migrate() :String {
        return "view/admin/migrate"
    }

    @Resource
    lateinit var migrationService:MigrationService

    /**
     * 流式日志：浏览器访问 /admin/migrate/execute
     */
    @PostMapping(value = ["/execute"], produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun execute(
        @RequestParam host: String?,
        @RequestParam port: Int,
        @RequestParam db: String?,
        @RequestParam username: String?,
        @RequestParam password: String?,
        response: HttpServletResponse
    ): StreamingResponseBody {
        response.contentType = MediaType.TEXT_HTML_VALUE
        return StreamingResponseBody { out: OutputStream? ->
            val w: PrintWriter = PrintWriter(OutputStreamWriter(out, StandardCharsets.UTF_8))
            w.println(
                """
                <!DOCTYPE html>
                <html lang="ch" xmlns:th="http://www.thymeleaf.org">
                <head>
                  <title>迁移日志</title>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width,initial-scale=1">
                  <link rel="stylesheet" th:href="@{/css/main.css}">
                <style>
                    body { margin: 0; padding: 0; font-family: Consolas, monospace; font-size: 14px }
                            pre { margin: 0; padding: 10px; white-space: pre-wrap; word-break: break-all }
                       </style>
                </head>
                <body>
                  <pre id="log">
                
                """.trimIndent()
            )
            w.flush()
            migrationService.migrate(MysqlConnDTO(host,port,db,username,password), w)
            w.println(
                """
                  </pre>
                  <script>
                    const log=document.getElementById('log');
                    const io=new IntersectionObserver(()=>log.scrollIntoView({behavior:'smooth'}));
                    io.observe(log);
                  </script>
                </body>
                </html>
                
                """.trimIndent()
            )
            w.flush()
        }
    }

}