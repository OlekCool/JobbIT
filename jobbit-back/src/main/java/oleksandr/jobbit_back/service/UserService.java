package oleksandr.jobbit_back.service;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.dto.RegisterRequest;
import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.entity.Role;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.exception.EmailAlreadyInUseException;
import oleksandr.jobbit_back.repository.CandidateProfileRepository;
import oleksandr.jobbit_back.repository.RecruiterProfileRepository;
import oleksandr.jobbit_back.repository.UserRepository;
import oleksandr.jobbit_back.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, CandidateProfileRepository candidateProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        // створити нового користувача з отриманих даних
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUserPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRegistrationDate(LocalDate.now());
        user.setLastPasswordChanged(LocalDateTime.now());
        user.setUserRole(registerRequest.getRole());
        user.setVerified(false);
        user.setVerificationToken(TokenGenerator.generateToken());

        // в залежності від ролі створити пустий профіль і підв'язати до користувача, зберегти профіль
        if (user.getUserRole() == Role.CANDIDATE) {
            CandidateProfile profile = new CandidateProfile();
            profile.setId(user.getId());
            profile.setUser(user);

            user.setCandidateProfile(profile);
            candidateProfileRepository.save(profile);
        } else if (user.getUserRole() == Role.RECRUITER) {
            RecruiterProfile profile = new RecruiterProfile();
            profile.setId(user.getId());
            profile.setUser(user);

            user.setRecruiterProfile(profile);
            recruiterProfileRepository.save(profile);
        }

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
