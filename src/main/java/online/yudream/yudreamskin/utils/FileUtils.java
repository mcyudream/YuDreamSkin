package online.yudream.yudreamskin.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class FileUtils {

    // 解压文件到临时目录，并返回解压后的文件路径列表
    public List<String> unzipFile(File zipFile) throws IOException {
        List<String> extractedFiles = new ArrayList<>();

        // 创建临时目录用于存放解压的文件
        File tempDir = createTempDirectory();
        System.out.println(tempDir.getAbsolutePath());

        // 创建ZipInputStream读取ZIP文件
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // 创建解压后的文件路径，保存在临时目录中
                File outputFile = new File(tempDir, entry.getName());

                // 确保父目录存在
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // 创建文件输出流
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                extractedFiles.add(outputFile.getAbsolutePath());

                zipInputStream.closeEntry();
            }
        }

        // 返回解压后的文件路径列表
        return extractedFiles;
    }

    // 创建临时目录
    private File createTempDirectory() throws IOException {
        // 获取系统默认临时目录
        Path tempDirPath = Files.createTempDirectory("temp");
        return tempDirPath.toFile();
    }

    // 删除临时目录及其内容
    public void deleteTempDirectory(File tempDir) {
        if (tempDir != null && tempDir.exists()) {
            for (File file : tempDir.listFiles()) {
                if (file.isDirectory()) {
                    deleteTempDirectory(file); // 递归删除子目录
                } else {
                    file.delete(); // 删除文件
                }
            }
            tempDir.delete(); // 删除空目录
        }
    }
}
