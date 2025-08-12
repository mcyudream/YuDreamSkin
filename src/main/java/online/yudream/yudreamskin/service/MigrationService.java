package online.yudream.yudreamskin.service;

import online.yudream.yudreamskin.entity.dto.MysqlConnDTO;

public interface MigrationService {

    void migrate(MysqlConnDTO dto);
}
