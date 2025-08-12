package online.yudream.yudreamskin;

import jakarta.annotation.Resource;
import online.yudream.yudreamskin.entity.dto.MysqlConnDTO;
import online.yudream.yudreamskin.service.MigrationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MigrateTest {
    @Resource
    private MigrationService migrationService;
    @Test
    void migrate() {
        MysqlConnDTO dto = new MysqlConnDTO("yudream.online",3306, "yudream_skin","skin","bYiPaDT6HD8fk644");
        migrationService.migrate(dto);
    }
}
