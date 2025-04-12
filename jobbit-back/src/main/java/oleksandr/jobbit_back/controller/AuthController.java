package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.dto.LoginRequest;
import oleksandr.jobbit_back.dto.RegisterRequest;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.service.UserService;
import oleksandr.jobbit_back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * REST-контролер для операцій аутентифікації, таких як реєстрація, вхід та верифікація облікового запису.
 * Забезпечує доступ до ендпоінтів для всіх користувачів.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Конструктор класу {@code AuthController}, що ініціалізує залежності:
     * {@link UserService}, {@link PasswordEncoder} та {@link JwtUtil}.
     *
     * @param userService Сервіс для роботи з користувачами.
     * @param passwordEncoder Кодувальник паролів для безпечного зберігання.
     * @param jwtUtil Утиліта для роботи з JWT-токенами.
     */
    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Ендпоінт для реєстрації нового користувача в системі.
     * Приймає запит з даними користувача та передає їх до сервісу реєстрації.
     * HTTP POST запит на `/api/auth/register`.
     *
     * @param registerRequest Об'єкт {@link RegisterRequest}, що містить дані для реєстрації користувача.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішну реєстрацію.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Ендпоінт для входу користувача в систему.
     * Перевіряє надані облікові дані та у разі успіху повертає JWT-токен та роль користувача.
     * HTTP POST запит на `/api/auth/login`.
     *
     * @param loginRequest Об'єкт {@link LoginRequest}, що містить email та пароль користувача.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та мапою, що містить токен (token) та роль (role) користувача.
     * Повертає статус 401 (UNAUTHORIZED) у разі невірних облікових даних.
     * @throws RuntimeException Якщо користувача з вказаним email не знайдено.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {

        User user = userService.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("No such user"));

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getUserRole());

        return ResponseEntity.ok(Map.of("token", token, "role", user.getUserRole().toString()));
    }

    /**
     * Ендпоінт для верифікації облікового запису користувача за наданим токеном.
     * Перевіряє токен, встановлює статус користувача як верифікований та видаляє токен верифікації.
     * HTTP GET запит на `/api/auth/verify` з параметром token.
     *
     * @param token Токен верифікації, надісланий на електронну пошту користувача.
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішну верифікацію.
     * Повертає статус 400 (BAD_REQUEST) у разі невірного токена.
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        User user = userService.findByVerificationToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невірний токен!");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        userService.verifyUser(user.getEmail());

        return ResponseEntity.ok("Акаунт підтверджено! Тепер ви можете увійти.");
    }
}
