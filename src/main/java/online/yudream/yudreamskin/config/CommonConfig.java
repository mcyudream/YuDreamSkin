package online.yudream.yudreamskin.config;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
public class CommonConfig {

    @Bean
    public Searcher searcher(){
        try (InputStream in = getClass().getResourceAsStream("/ipdb/ip2region.xdb")) {
            Objects.requireNonNull(in, "ip2region.xdb 不存在");
            byte[] dbBuff = in.readAllBytes();
            return Searcher.newWithBuffer(dbBuff); // 内存模式
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
