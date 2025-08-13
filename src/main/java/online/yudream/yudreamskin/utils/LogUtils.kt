package online.yudream.yudreamskin.utils

import jakarta.servlet.http.HttpServletResponse
import online.yudream.yudreamskin.entity.dto.MysqlConnDTO
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

@Component
class LogUtils {

    fun log(response: HttpServletResponse,logGenerator: (PrintWriter) -> Unit) : StreamingResponseBody{
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
            try {
                logGenerator(w)
            } catch (e: Exception) {
                w.println("发生错误: ${e.message}")
            }
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
