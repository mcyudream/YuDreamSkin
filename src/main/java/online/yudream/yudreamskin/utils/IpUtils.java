package online.yudream.yudreamskin.utils;

import jakarta.annotation.Resource;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class IpUtils {

    @Resource
    private Searcher searcher;

    public String getIpAddress(String ip){
        if ("127.0.0.1".equals(ip) || ip.startsWith("192.168")) {
            return "局域网 ip";
        }
        if (searcher == null) {
            try {
                File file = ResourceUtils.getFile("classpath:ipdb/ip2region.xdb");
                String dbPath = file.getPath();
                searcher = Searcher.newWithFileOnly(dbPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String region = null;
        String errorMessage = null;
        try {
            region = searcher.search(ip);
            return region;
        } catch (Exception e) {
            errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.length() > 256) {
                errorMessage = errorMessage.substring(0,256);
            }
            e.printStackTrace();
            return errorMessage;
        }
    }


}
