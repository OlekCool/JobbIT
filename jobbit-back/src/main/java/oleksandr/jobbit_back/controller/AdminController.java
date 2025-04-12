package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контролер для операцій, доступних лише адміну.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    /**
     * Конструктор класу {@code AdminController}, що ініціалізує залежність {@link UserService}.
     *
     * @param userService Об'єкт {@link UserService} для роботи з даними користувачів.
     * Dependency Injection
     */
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Ендпоінт для отримання списку всіх зареєстрованих користувачів.
     * Доступний лише користувачам з роллю 'ADMIN'.
     * HTTP GET запит на `/api/admin/users`.
     *
     * @return {@link ResponseEntity} зі статусом 200 (OK) та списком об'єктів {@link User} у тілі відповіді.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
