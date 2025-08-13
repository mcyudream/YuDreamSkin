package online.yudream.yudreamskin.service;

import online.yudream.yudreamskin.entity.dto.MysqlConnDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public interface MigrationService {

    void migrateSkinsFile(List<File> files, PrintWriter w);

    void migrate(MysqlConnDTO dto, PrintWriter w );
}
