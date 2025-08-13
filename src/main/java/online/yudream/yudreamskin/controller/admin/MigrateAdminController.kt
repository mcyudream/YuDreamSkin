package online.yudream.yudreamskin.controller.admin

import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletResponse
import online.yudream.yudreamskin.entity.dto.MysqlConnDTO
import online.yudream.yudreamskin.service.MigrationService
import online.yudream.yudreamskin.utils.FileUtils
import online.yudream.yudreamskin.utils.LogUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.File


@Controller
@RequestMapping("/admin/migrate")
class MigrateAdminController {

    @GetMapping
    fun migrate() :String {
        return "view/admin/migrate"
    }

    @Resource
    lateinit var migrationService:MigrationService
    @Resource
    lateinit var logUtils: LogUtils
    @Resource
    lateinit var fileUtils: FileUtils
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
        return logUtils.log(response){ w-> migrationService.migrate(MysqlConnDTO(host,port,db,username,password),w) }
    }


    @PostMapping(value = ["/executeTextures"], produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun executeTextures(
        @RequestParam file : MultipartFile,
        response: HttpServletResponse
    ): StreamingResponseBody {
        val tempZipFile: File? = File.createTempFile("textures-", ".zip")
        file.transferTo(tempZipFile)
        val files = fileUtils.unzipFile(tempZipFile).stream().map {
            File(it)
        }.toList()

        return logUtils.log(response){ w-> migrationService.migrateSkinsFile(files,w) }
    }

}