package online.yudream.yudreamskin.service;

public interface CaptchaService {
    void sendCaptcha(String email, String type, String option);
}
