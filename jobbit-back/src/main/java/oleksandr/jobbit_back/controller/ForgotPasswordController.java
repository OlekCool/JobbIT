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
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    private final UserService userService;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ForgotPasswordController(UserService userService, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        fp.setExpirationTime(new Date(System.currentTimeMillis() + 600 * 1000));
        fp.setUser(user);

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        System.out.println(fp);

        return ResponseEntity.ok("Email sent for verification");
    }

    @PostMapping("/sendotp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("The email must be valid"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        forgotPasswordRepository.deleteById(fp.getFpid());
        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/newpassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@PathVariable String email,
                                                        @RequestBody ChangePassword changePassword) {
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userService.updatePassword(email, encodedPassword);
        userService.updatePasswordChangingData(email, LocalDateTime.now());

        return ResponseEntity.ok("Password has been changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }

}
