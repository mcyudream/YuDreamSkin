package online.yudream.yudreamskin.service;

import online.yudream.yudreamskin.entity.dto.MysqlConnDTO;

import java.io.PrintWriter;

public interface MigrationService {

    void migrate(MysqlConnDTO dto, PrintWriter w );
}
