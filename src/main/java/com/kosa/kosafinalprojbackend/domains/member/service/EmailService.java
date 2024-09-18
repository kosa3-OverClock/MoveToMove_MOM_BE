package com.kosa.kosafinalprojbackend.domains.member.service;

import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    // 인증 코드 보내기
    @Async
    public void sendVerificationCode(String email, String verificationCode) {
        String subject = "Move-tO-Move 비밀번호 찾기 인증번호";
        String content = htmlContent(verificationCode);

        try {
            sendEmail(email, subject, content);
            log.info("====>>>>>>>>>> {}: 인증코드 전송 성공", email);
        } catch (MessagingException e) {
            log.info("====>>>>>>>>>> {}: 인증코드 전송 실패", email);
            throw new RuntimeException(e);
        }
    }


    // 인증코드 HTML 만들기
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


    // 프로젝트 초대
    @Async
    public void projectInvite(String projectName, List<MemberDto> memberDtoList) {
        memberDtoList.forEach(member -> {
            String email = member.getEmail();
            String subject = "Move-tO-Move" + projectName + " 프로젝트 초대";
            String content = projectInviteHtml(projectName);

            try {
                sendEmail(email, subject, content);
                log.info("====>>>>>>>>>> {}: 이메일 전송 성공", email);
            } catch (MessagingException e) {
                log.info("====>>>>>>>>>> {}: 이메일 전송 실패", email);
                throw new RuntimeException(e);
            }
        });
    }


    // 프로젝트 초대 HTML
    private String projectInviteHtml(String projectName) {
        return "<html>" +
            "<head>" +
            "  <style>" +
            "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
            "    .email-container { max-width: 600px; margin: 40px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
            "    .email-header { background-color: #28a745; color: #ffffff; padding: 10px; border-radius: 8px 8px 0 0; text-align: center; }" +
            "    .email-body { padding: 20px; font-size: 16px; line-height: 1.5; color: #333333; }" +
            "    .email-footer { text-align: center; font-size: 12px; color: #777777; margin-top: 20px; }" +
            "    .invite-message { font-size: 18px; font-weight: bold; color: #333333; text-align: center; margin: 20px 0; padding: 10px; background-color: #f2f2f2; border-radius: 4px; }" +
            "    .project-name { font-size: 22px; font-weight: bold; color: #007bff; text-align: center; margin: 20px 0; }" +
            "    .cta-button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff !important; background-color: #6b9e9b; text-decoration: none; border-radius: 5px; margin-top: 20px; text-align: center; font-weight: bold; }" +
            "    .cta-button:hover { background-color: #4a7875; }" +
            "  </style>" +
            "</head>" +
            "<body>" +
            "  <div class='email-container'>" +
            "    <div class='email-header'>" +
            "      <h1>프로젝트 초대</h1>" +
            "    </div>" +
            "    <div class='email-body'>" +
            "      <p>안녕하세요,</p>" +
            "      <p>Move-tO-Move에서 새로운 프로젝트에 초대되었습니다. 아래 프로젝트 이름을 확인하시고 참여해 주세요.</p>" +
            "      <div class='project-name'>" + projectName + "</div>" +
            "      <p>프로젝트에 참여하려면 아래 버튼을 클릭하세요:</p>" +
            "      <div style='text-align: center;'>" +
            "        <a href='https://www.move-to-move.online' class='cta-button'>프로젝트 참여하기</a>" +
            "      </div>" +
            "      <p>이 초대를 요청하지 않았다면, 이 이메일을 무시해 주세요.</p>" +
            "      <p>감사합니다.<br>Move-tO-Move 팀</p>" +
            "    </div>" +
            "    <div class='email-footer'>" +
            "      <p>&copy; 2024 Move-tO-Move. All rights reserved.</p>" +
            "    </div>" +
            "  </div>" +
            "</body>" +
            "</html>";
    }
}
