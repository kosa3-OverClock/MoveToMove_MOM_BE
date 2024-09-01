package com.kosa.kosafinalprojbackend.domains.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public String sendVerificationCode(String email) throws MessagingException {
        String verificationCode = generateVerificationCode();
        String subject = "Move-tO-Move 비밀번호 찾기 인증번호";
        String content = htmlContent(verificationCode);

        sendEmail(email, subject, content);

        return verificationCode;
    }


    // 랜덤 6자리 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 코드 생성
        return String.valueOf(code);
    }


    // HTML 만들기
    private String htmlContent(String verificationCode) {
        return "<html>" +
            "<head>" +
            "  <style>" +
            "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
            "    .email-container { max-width: 600px; margin: 40px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
            "    .email-header { background-color: #007bff; color: #ffffff; padding: 10px; border-radius: 8px 8px 0 0; text-align: center; }" +
            "    .email-body { padding: 20px; }" +
            "    .email-footer { text-align: center; font-size: 12px; color: #777777; margin-top: 20px; }" +
            "    .verification-code { font-size: 24px; font-weight: bold; color: #333333; text-align: center; margin: 20px 0; padding: 10px; background-color: #f2f2f2; border-radius: 4px; }" +
            "  </style>" +
            "</head>" +
            "<body>" +
            "  <div class='email-container'>" +
            "    <div class='email-header'>" +
            "      <h1>이메일 인증번호</h1>" +
            "    </div>" +
            "    <div class='email-body'>" +
            "      <p>인증번호: </p>" +
            "      <div class='verification-code'>" + verificationCode + "</div>" +
            "      <p>이 인증번호를 요청한 적이 없다면, 메일을 무시하세요.</p>" +
            "      <p>감사합니다. <br>Move-tO-Move</p>" +
            "    </div>" +
            "    <div class='email-footer'>" +
            "      <p>&copy; 2024 Move-tO-Move. All rights reserved.</p>" +
            "    </div>" +
            "  </div>" +
            "</body>" +
            "</html>";
    }


    // 이메일 보내기
    private void sendEmail(String email, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);   // 두번째 파라미터 HTML 설정

        mailSender.send(mimeMessage);
    }
}
