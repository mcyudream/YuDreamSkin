package online.yudream.yudreamskin.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * online.yudream.platform.utils
 *
 * @author SiberianHusky
 * * @date 2025/7/7
 */
@Component
public class UUIDUtils {
    public  String generateNoSymbolUUID(String name) {
        return generateUUID(name).replace("-", "");
    }

    public  String generateUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)).toString();
    }
}
