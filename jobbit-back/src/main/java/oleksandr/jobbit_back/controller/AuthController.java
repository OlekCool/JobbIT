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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {

        User user = userService.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("No such user"));

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getUserRole());

        return ResponseEntity.ok(Map.of("token", token, "role", user.getUserRole().toString()));
    }

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
