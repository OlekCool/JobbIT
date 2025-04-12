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

/**
 * Сервіс {@code EmailService} відповідає за надсилання електронних листів у застосунку.
 * Використовує {@link JavaMailSender} для надсилання простих текстових повідомлень та MIME-повідомлень (з HTML).
 * Конфігурація поштових налаштувань (наприклад, ім'я відправника) отримується з {@link Environment}.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Environment environment;

    /**
     * Конструктор класу {@code EmailService}, що ініціалізує залежності.
     *
     * @param javaMailSender Інструмент для надсилання електронних листів. Автоматично впроваджується Spring.
     * @param environment    Інтерфейс для доступу до конфігураційних властивостей застосунку.
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public EmailService(JavaMailSender javaMailSender, Environment environment) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
    }

    /**
     * Надсилає просте текстове електронне повідомлення.
     *
     * @param mailBody Об'єкт {@link MailBody}, що містить інформацію про отримувача, тему та текст листа.
     */
    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());

        javaMailSender.send(message);
    }

    /**
     * Надсилає MIME-електронне повідомлення, яке може містити HTML-контент.
     *
     * @param to Адреса електронної пошти отримувача.
     * @param subject Тема електронного листа.
     * @param text Вміст електронного листа. Може містити HTML-теги.
     * @throws RuntimeException Якщо виникає помилка під час створення або надсилання MIME-повідомлення.
     */
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

    /**
     * Надсилає електронний лист для верифікації облікового запису користувача.
     * Містить посилання, яке користувач повинен натиснути для підтвердження своєї електронної пошти.
     *
     * @param user Об'єкт {@link User}, якому потрібно надіслати лист для верифікації.
     */
    public void sendVerificationEmail(User user) {
        String subject = "Підтвердження реєстрації";
        String url = "http://localhost:8081/auth/verify?token=" + user.getVerificationToken();
        String message = "Привіт, " + user.getEmail() + "!<br>"
                + "Будь ласка, натисни <a href=\"" + url + "\">сюди</a>, щоб підтвердити акаунт.";

        sendMimeMessage(user.getEmail(), subject, message);
    }
}
