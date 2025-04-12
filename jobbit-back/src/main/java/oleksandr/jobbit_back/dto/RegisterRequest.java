package oleksandr.jobbit_back.dto;

import lombok.Data;
import oleksandr.jobbit_back.entity.Role;

/**
 * DTO (Data Transfer Object) для представлення запиту на реєстрацію користувача в системі.
 * Містить email та пароль користувача, а також його роль.
 * Використовується для передачі даних від клієнта до сервера при реєстрації.
 *
 * @author Oleksandr Borovyk
 */
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;

    /**
     * Конструктор за замовчуванням для об'єкта {@code RegisterRequest}.
     * Не приймає жодних аргументів.
     */
    public RegisterRequest() {
    }

    /**
     * Конструктор для створення об'єкта {@code RegisterRequest} з вказаними email та паролем.
     *
     * @param email Електронна пошта користувача.
     * @param password Пароль користувача.
     * @param role Роль користувача.
     */
    public RegisterRequest(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Гетер для поля email.
     * @return email Email користувача.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Гетер для поля password.
     * @return password Пароль користувача.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Гетер для поля role.
     * @return role Роль користувача.
     */
    public Role getRole() {
        return role;
    }

}
