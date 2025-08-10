package online.yudream.yudreamskin.utils;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;


@Component
public class MinioUtils {


    @Value("${spring.data.minio.bucket}")
    private String minioBucket;
    @Resource
    private MinioClient minioClient;
    @Resource
    private String minioUrl;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public void initBucket(){
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioBucket).build());
            } else {
                System.out.println("Bucket '" + minioBucket + "' already exists.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String uploadFile(MultipartFile file){
        try {
            String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            String fileName = md5 + ":md5:" + file.getOriginalFilename();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .contentType("application/octet-stream")
                    .object(fileName)
                    .stream(file.getInputStream(),file.getInputStream().available(),-1
            ).build());
            return fileName;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public String getPreviewUrl(String fileName) {
        return getPreviewUrl(fileName, "avatar");
    }

    public String getPreviewUrl(String fileName, String type) {
        try {
            if (stringRedisTemplate.hasKey("previewUrl:"+fileName)) {
                return stringRedisTemplate.opsForValue().get("previewUrl:"+fileName);
            }
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucket)
                            .object(fileName)
                            .expiry(3600)
                            .build()

            );
            stringRedisTemplate.opsForValue().set("previewUrl:" +fileName, url, 3600, TimeUnit.SECONDS);
            return url;
        } catch (Exception e) {
            switch (type) {
                case "bg":
                    return "/assets/images/bg.png";
                default:
                    return "/assets/images/default_avtar.png";
            }

        }
    }
}
