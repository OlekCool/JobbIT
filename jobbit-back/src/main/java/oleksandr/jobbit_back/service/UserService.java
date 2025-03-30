package oleksandr.jobbit_back.service;

import oleksandr.jobbit_back.dto.RegisterRequest;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.repository.UserRepository;
import oleksandr.jobbit_back.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUserPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRegistrationDate(LocalDate.now());
        user.setLastPasswordChanged(LocalDateTime.now());
        user.setUserRole(registerRequest.getRole());
        user.setVerified(false);
        user.setVerificationToken(TokenGenerator.generateToken());

        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void updatePassword(String email, String password) {
        userRepository.updatePassword(email, password);
    }

    public void updatePasswordChangingData(String email, LocalDateTime date) {
        userRepository.updatePasswordChangingData(email, date);
    }

    public User findByVerificationToken(String verificationToken) {
        return userRepository.findByVerificationToken(verificationToken);
    }

    public void verifyUser(String email) {
        userRepository.verifyUser(email);
    }
}
