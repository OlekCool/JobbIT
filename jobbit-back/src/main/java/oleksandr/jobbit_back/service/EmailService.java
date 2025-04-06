package oleksandr.jobbit_back.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import oleksandr.jobbit_back.dto.MailBody;
import oleksandr.jobbit_back.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Environment environment;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, Environment environment) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());

        javaMailSender.send(message);
    }

    public void sendMimeMessage(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Помилка надсилання email: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Підтвердження реєстрації";
        String url = "http://localhost:8081/auth/verify?token=" + user.getVerificationToken();
        String message = "Привіт, " + user.getEmail() + "!<br>" +
                "Будь ласка, натисни <a href=\"" + url + "\">сюди</a>, щоб підтвердити акаунт.";

        sendMimeMessage(user.getEmail(), subject, message);
    }
}
