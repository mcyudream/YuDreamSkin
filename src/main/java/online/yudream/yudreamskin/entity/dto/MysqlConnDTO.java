package online.yudream.yudreamskin.entity.dto;

import lombok.Data;


public record MysqlConnDTO(
        String host,
        Integer port,
        String database,
        String username,
        String password) {}