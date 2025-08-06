package online.yudream.yudreamskin.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MailUtils {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.username}")
    private String from ;// 发件人
    /**
     * 发送html的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendHtmlEmail(String subject, String content, String... to){
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
        // 设置邮件内容
        helper.setText(content, true);

        // 发送邮件
        mailSender.send(mimeMessage);

        log.info("发送邮件成功");
        return true;
    }

    @SneakyThrows(Exception.class)
    public void sendCodeEmail(String option, String type, String to){
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("captcha:"+type+":"+to);
        // 初始检查
        String lastSendTimestamp = hashOps.get("lastSendTimestamp");
        String sendCount = hashOps.get("sendCount");

        if (StringUtils.isNotBlank(sendCount) && Integer.parseInt(sendCount) >= 5) {
            hashOps.expire(24, TimeUnit.HOURS);
            throw new RuntimeException("验证码发送过于频繁");
        }
        if (StringUtils.isNotBlank(lastSendTimestamp)) {
            long lastSendTime = Long.parseLong(lastSendTimestamp);
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastSendTime;
            if (elapsedTime < 60 * 1000) {
                throw new RuntimeException("验证码发送过于频繁");
            }
        }
        int newSendCount = StringUtils.isNotBlank(sendCount) ? Integer.parseInt(sendCount) + 1 : 1;
        String captcha = RandomStringUtils.randomNumeric(6);

        try {
            sendCaptcha(to, captcha, option);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashOps.put("captcha", captcha);
        hashOps.put("lastSendTimestamp", String.valueOf(System.currentTimeMillis()));
        hashOps.put("sendCount", String.valueOf(newSendCount));
        hashOps.expire(5, TimeUnit.MINUTES); // 设置过期时间为5分钟


    }
    public boolean viaCaptcha(String to, String captcha, String type) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("captcha:"+type+":"+to);
        String code = hashOps.get("captcha");
        return code != null && code.equals(captcha);
    }

    private void sendCaptcha(String to, String captcha, String option) throws Exception {
        // 根据hashKey判断是发送邮件还是短信，然后调用相应的发送方法

            if (sendHtmlEmail( option+ "验证" ,
                    "<html><body>用户你好，你的验证码是:<h1>%s</h1>请在五分钟内完成%s</body></html>".formatted(captcha,option), to)) {
            }


    }
}
