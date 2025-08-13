package online.yudream.yudreamskin.utils;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

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

    public String uploadFile(File file, String filetype){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            // 计算文件的 MD5 值
            String md5 = DigestUtils.md5DigestAsHex(fileInputStream);

            // 获取文件名，使用MD5作为前缀
            String fileName = md5 + ":md5:" + file.getName() + "." + filetype;

            // 获取文件大小
            long fileSize = file.length();

            // 重新打开文件输入流，因为它已经在计算MD5时被消费了
            fileInputStream = new FileInputStream(file);

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .contentType("application/octet-stream")
                    .object(fileName)
                    .stream(fileInputStream, fileSize, -1)
                    .build());

            // 返回上传的文件名
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getPreviewUrl(String fileName) {
        return getPreviewUrl(fileName, "avatar");
    }

    public String getPreviewUrl(String fileName, String type) {
            try {
                // 1. 先判断对象是否存在
                minioClient.statObject(
                        StatObjectArgs.builder()
                                .bucket(minioBucket)
                                .object(fileName)
                                .build());
                // 2. 存在则生成 URL
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(minioBucket)
                                .object(fileName)
                                .expiry(3600)
                                .build());

        } catch (Exception e) {
            return switch (type) {
                case "skin" -> "/assets/images/skin.png";
                case "bg" -> "/assets/images/bg.png";
                default -> "/assets/images/default_avtar.png";
            };

        }
    }
}
