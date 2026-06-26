package com.duy.do_an_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Mã OTP đổi mật khẩu");
            message.setText("Mã OTP của bạn là: " + otp + "\n\nMã này có hiệu lực trong 10 phút.");
            mailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException("Không gửi được email OTP. Kiểm tra cấu hình mail SMTP hoặc thông tin đăng nhập Gmail.", ex);
        }
    }
}
