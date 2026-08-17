package com.whaa.shop.auth.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaa.shop.common.exception.BusinessException;
import com.whaa.shop.user.domain.User;
import com.whaa.shop.user.infrastructure.UserMapper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Locale;

@Service
public class PasswordResetService {
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private final UserMapper users;
    private final PasswordEncoder encoder;
    private final StringRedisTemplate redis;
    private final JavaMailSender mail;
    private final String mailUsername;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(UserMapper users, PasswordEncoder encoder, StringRedisTemplate redis,
                                JavaMailSender mail, @Value("${spring.mail.username:}") String mailUsername) {
        this.users = users;
        this.encoder = encoder;
        this.redis = redis;
        this.mail = mail;
        this.mailUsername = mailUsername == null ? "" : mailUsername.trim().toLowerCase(Locale.ROOT);
    }

    public void sendCode(String username, String email) {
        User user = find(username, email);
        if (user == null) return;
        if (mailUsername.isBlank() || !mailUsername.contains("@")) {
            throw new BusinessException("邮件服务未正确配置 MAIL_USERNAME");
        }
        String rateKey = "password-reset:rate:" + user.getId();
        if (!Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(rateKey, "1", Duration.ofSeconds(60)))) {
            throw new BusinessException("验证码发送过于频繁，请稍后再试");
        }
        String code = String.format("%06d", random.nextInt(1_000_000));
        String codeKey = "password-reset:code:" + user.getId();
        redis.opsForValue().set(codeKey, encoder.encode(code), Duration.ofMinutes(10));
        try {
            MimeMessage message = mail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(mailUsername, "橙选商城");
            helper.setTo(user.getEmail());
            helper.setSubject("橙选商城密码重置验证码");
            helper.setText("您的密码重置验证码为：" + code
                    + "\n\n验证码 10 分钟内有效。如非本人操作，请忽略此邮件。", false);
            mail.send(message);
        } catch (Exception e) {
            log.error("Password reset email delivery failed: mailServerUser={}, recipientDomain={}", mailUsername, emailDomain(user.getEmail()), e);
            redis.delete(codeKey);
            redis.delete(rateKey);
            throw new BusinessException("验证码邮件发送失败，请确认 QQ 邮箱已开启 SMTP、使用授权码，且发件人与 MAIL_USERNAME 一致");
        }
    }

    public void reset(String username, String email, String code, String password) {
        User user = find(username, email);
        if (user == null) throw new BusinessException("用户名或邮箱不匹配");
        String key = "password-reset:code:" + user.getId();
        String hash = redis.opsForValue().get(key);
        if (hash == null || !encoder.matches(code, hash)) throw new BusinessException("验证码无效或已过期");
        user.setPasswordHash(encoder.encode(password));
        users.updateById(user);
        redis.delete(key);
    }

    private User find(String username, String email) {
        return users.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username.trim())
                .eq(User::getEmail, email.trim().toLowerCase(Locale.ROOT))
                .eq(User::getEnabled, true));
    }

    private String emailDomain(String email) {
        int at = email == null ? -1 : email.lastIndexOf('@');
        return at < 0 ? "unknown" : email.substring(at + 1);
    }
}
