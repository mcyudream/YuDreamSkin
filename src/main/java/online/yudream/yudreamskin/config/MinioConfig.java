package online.yudream.yudreamskin.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${spring.data.minio.host}")
    private String minioHost;
    @Value("${spring.data.minio.port}")
    private Integer minioPort = 9000;
    @Value("${spring.data.minio.username}")
    private String minioUsername;
    @Value("${spring.data.minio.password}")
    private String minioPassword;

    @Bean
    public MinioClient getMinioClient(){
        return MinioClient.builder()
                .credentials(minioUsername, minioPassword)
                .endpoint(getMinioUrl())
                .build();
    }

    @Bean
    public String getMinioUrl(){
        System.out.println(minioHost);
        System.out.println(minioPort);
        if (minioHost.contains(":")){
            return minioHost;
        } else  {
            return "http://"+minioHost + ":" + minioPort;
        }
    }
}
