package online.yudream.yudreamskin;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import online.yudream.yudreamskin.service.InitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YuDreamSkinApplication {
    @Resource
    private InitService initService;

    @PostConstruct
    public void init(){
        initService.initApp();
    }

    public static void main(String[] args) {
        SpringApplication.run(YuDreamSkinApplication.class, args);
    }

}
