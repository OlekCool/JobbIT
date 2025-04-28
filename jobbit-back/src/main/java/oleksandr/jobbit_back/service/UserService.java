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
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервіс {@code UserService} відповідає за бізнес-логіку, пов'язану з користувачами, включаючи реєстрацію,
 * пошук, оновлення пароля, верифікацію облікового запису та отримання списку всіх користувачів.
 * Використовує репозиторії {@link UserRepository}, {@link CandidateProfileRepository},
 * {@link RecruiterProfileRepository} для взаємодії з базою даних, {@link PasswordEncoder} для кодування паролів
 * та {@link EmailService} для надсилання електронних листів.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс, що робить його доступним для впровадження залежностей.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Конструктор класу {@code UserService}, що ініціалізує залежності.
     *
     * @param userRepository              Репозиторій для роботи з сутностями {@link User}.
     * @param candidateProfileRepository Репозиторій для роботи з профілями кандидатів ({@link CandidateProfile}).
     * @param recruiterProfileRepository Репозиторій для роботи з профілями рекрутерів ({@link RecruiterProfile}).
     * @param passwordEncoder             Кодувальник паролів для безпечного зберігання.
     * @param emailService                Сервіс для надсилання електронних листів.
     */
    @Autowired
    public UserService(UserRepository userRepository, CandidateProfileRepository candidateProfileRepository,
                       RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Реєструє нового користувача в системі. Перевіряє унікальність електронної пошти, створює обліковий запис
     * користувача, пов'язаний профіль (залежно від ролі), зберігає користувача та відправляє йому
     * верифікаційний лист. Транзакційна операція.
     *
     * @param registerRequest DTO ({@link RegisterRequest}), що містить дані для реєстрації користувача.
     * @throws EmailAlreadyInUseException Якщо вказана електронна пошта вже зареєстрована в системі.
     */
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

    /**
     * Знаходить користувача за його електронною поштою.
     *
     * @param email Електронна пошта користувача для пошуку.
     * @return {@link Optional<User>} з знайденим користувачем або порожній {@link Optional}, якщо користувача не знайдено.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Оновлює пароль користувача з вказаною електронною поштою.
     *
     * @param email Електронна пошта користувача, пароль якого потрібно оновити.
     * @param password Новий пароль користувача (вже закодований).
     */
    public void updatePassword(String email, String password) {
        userRepository.updatePassword(email, password);
    }

    /**
     * Оновлює дату та час останньої зміни пароля для користувача з вказаною електронною поштою.
     *
     * @param email Електронна пошта користувача, для якого потрібно оновити час зміни пароля.
     * @param date  Нова дата та час останньої зміни пароля.
     */
    public void updatePasswordChangingData(String email, LocalDateTime date) {
        userRepository.updatePasswordChangingData(email, date);
    }

    /**
     * Знаходить користувача за його токеном верифікації.
     *
     * @param verificationToken Токен верифікації користувача.
     * @return Знайдений об'єкт {@link User} або {@code null}, якщо користувача з таким токеном не знайдено.
     */
    public User findByVerificationToken(String verificationToken) {
        return userRepository.findByVerificationToken(verificationToken);
    }

    /**
     * Верифікує обліковий запис користувача за його електронною поштою.
     *
     * @param email Електронна пошта користувача, обліковий запис якого потрібно верифікувати.
     */
    public void verifyUser(String email) {
        userRepository.verifyUser(email);
    }

    /**
     * Отримує список усіх зареєстрованих користувачів.
     *
     * @return Список усіх об'єктів {@link User}, знайдених у базі даних.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Користувача з ID " + id + " не знайдено"));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
