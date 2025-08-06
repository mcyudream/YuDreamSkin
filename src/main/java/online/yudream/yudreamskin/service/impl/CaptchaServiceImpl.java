package online.yudream.yudreamskin.service.impl;

import jakarta.annotation.Resource;
import online.yudream.yudreamskin.service.CaptchaService;
import online.yudream.yudreamskin.utils.MailUtils;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    private MailUtils  mailUtils;

    @Override
    public void sendCaptcha(String email, String type, String option) {
        mailUtils.sendCodeEmail(option,type,email);
    }
}
