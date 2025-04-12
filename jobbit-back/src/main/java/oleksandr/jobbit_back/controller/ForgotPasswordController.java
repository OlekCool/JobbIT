package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.dto.MailBody;
import oleksandr.jobbit_back.entity.ForgotPassword;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.repository.ForgotPasswordRepository;
import oleksandr.jobbit_back.service.EmailService;
import oleksandr.jobbit_back.service.UserService;
import oleksandr.jobbit_back.utils.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

/**
 * REST-контролер для обробки запитів, пов'язаних із відновленням забутого пароля.
 * Включає ендпоінти для верифікації електронної пошти, підтвердження OTP та встановлення нового пароля.
 * Доступ до ендпоінтів здійснюється без авторизації.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    private final UserService userService;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор класу {@code ForgotPasswordController}, що ініціалізує необхідні залежності.
     *
     * @param userService              Сервіс для роботи з користувачами. Автоматично впроваджується Spring.
     * @param emailService             Сервіс для надсилання електронних листів. Автоматично впроваджується Spring.
     * @param forgotPasswordRepository Репозиторій для роботи з даними відновлення пароля. Автоматично впроваджується Spring.
     * @param passwordEncoder          Кодувальник паролів. Автоматично впроваджується Spring.
     */
    @Autowired
    public ForgotPasswordController(UserService userService, EmailService emailService,
                                    ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Ендпоінт для верифікації наданої електронної пошти користувача при запиті відновлення пароля.
     * Генерує одноразовий пароль (OTP), надсилає його на вказану електронну пошту та зберігає OTP у базі даних.
     * HTTP POST запит на `/api/auth/forgotpassword/verifyMail/{email}`.
     *
     * @param email Електронна пошта користувача, який запитує відновлення пароля. Передається як параметр шляху.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішне надсилання OTP на електронну пошту.
     * @throws RuntimeException Якщо вказана електронна пошта не є валідною та її нема у базі користувачів.
     */
    @PostMapping("/forgotpassword/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("The email must be valid"));

        int otp = otpGenerator();
        MailBody mailBody = new MailBody();
        mailBody.setTo(email);
        mailBody.setText("This is the OTP for your forgot-password request: " + otp);
        mailBody.setSubject("OTP for Forgot password request");

        ForgotPassword fp = new ForgotPassword();
        fp.setOtp(otp);
        fp.setExpirationDate(new Date(System.currentTimeMillis() + 600 * 1000));
        fp.setUser(user);

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        System.out.println(fp);

        return ResponseEntity.ok("Email sent for verification");
    }

    /**
     * Ендпоінт для підтвердження наданого користувачем одноразового пароля (OTP).
     * Перевіряє відповідність OTP збереженому в базі даних для вказаного користувача та термін його дії.
     * У разі успішної верифікації видаляє OTP з бази даних.
     * HTTP POST запит на `/api/auth/sendotp/{otp}/{email}`.
     *
     * @param otp   Одноразовий пароль, введений користувачем. Передається як параметр шляху.
     * @param email Електронна пошта користувача, для якого перевіряється OTP. Передається як параметр шляху.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішне підтвердження OTP.
     * Повертає статус 417 (EXPECTATION_FAILED) якщо час очікування OTP вийшов,
     * @throws RuntimeException Якщо вказаний OTP не відповідає збереженому для даної електронної пошти.
     */
    @PostMapping("/sendotp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("The email must be valid"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        forgotPasswordRepository.deleteById(fp.getFpid());
        return ResponseEntity.ok("OTP verified!");
    }

    /**
     * Ендпоінт для обробки запиту на встановлення нового пароля користувача.
     * Приймає новий пароль, кодує його та оновлює пароль користувача в базі даних.
     * Також оновлює час останньої зміни пароля.
     * HTTP POST запит на `/api/auth/newpassword/{email}` з тілом запиту у форматі {@link ChangePassword}.
     *
     * @param email Електронна пошта користувача, для якого встановлюється новий пароль. Передається як параметр шляху.
     * @param changePassword Об'єкт {@link ChangePassword}, що містить новий пароль. Передається в тілі запиту.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішну зміну пароля.
     */
    @PostMapping("/newpassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email,
                                                        @RequestBody ChangePassword changePassword) {
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userService.updatePassword(email, encodedPassword);
        userService.updatePasswordChangingData(email, LocalDateTime.now());

        return ResponseEntity.ok("Password has been changed successfully");
    }

    /**
     * Приватний утилітний метод для генерації шестизначного одноразового пароля (OTP).
     *
     * @return Згенерований шестизначний OTP у вигляді цілого числа.
     */
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }

}
