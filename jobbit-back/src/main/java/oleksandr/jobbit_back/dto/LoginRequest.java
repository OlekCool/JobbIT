package oleksandr.jobbit_back.dto;

/**
 * DTO (Data Transfer Object) для представлення запиту на вхід користувача в систему.
 * Містить email та пароль користувача.
 * Використовується для передачі даних від клієнта до сервера при аутентифікації.
 *
 * @author Oleksandr Borovyk
 */
public class LoginRequest {
    private String email;
    private String password;

    /**
     * Конструктор за замовчуванням для об'єкта {@code LoginRequest}.
     * Не приймає жодних аргументів.
     */
    public LoginRequest() {
    }

    /**
     * Конструктор для створення об'єкта {@code LoginRequest} з вказаними email та паролем.
     *
     * @param email    Електронна пошта користувача.
     * @param password Пароль користувача.
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Гетер для поля email.
     * @return email Email користувача.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Сетер для поля email.
     * @param email Email користувача.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Гетер для поля password.
     * @return password Пароль користувача.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Сетер для поля password.
     * @param password Пароль користувача.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
