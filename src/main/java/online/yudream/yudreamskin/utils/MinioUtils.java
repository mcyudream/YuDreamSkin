package online.yudream.yudreamskin.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;


@Component
public class MinioUtils {


    @Value("${spring.data.minio.bucket}")
    private String minioBucket;
    @Resource
    private MinioClient minioClient;
    @Resource
    private String minioUrl;

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

    public String getFile(String fileName){
        return minioUrl + "/" + minioBucket + "/" + fileName;
    }
}
