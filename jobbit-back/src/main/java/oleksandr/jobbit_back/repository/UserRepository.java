package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Репозиторій {@code UserRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link User}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code User}, а також містить власні методи для виконання специфічних запитів.
 * Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA репозиторій, що дозволяє Spring
 * автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Знаходить користувача за його електронною поштою.
     * Повертає {@link Optional<User>}, який може містити знайденого користувача або бути порожнім,
     * якщо користувача з вказаною електронною поштою не знайдено.
     *
     * @param email Електронна пошта користувача, якого потрібно знайти.
     * @return {@link Optional<User>} з знайденим користувачем або порожній {@link Optional}.
     */
    Optional<User> findByEmail(String email);

    /**
     * Метод для знаходження користувача за певним id
     * @param id ідентифікатор користувача
     * @return користувача
     */
    Optional<User> findById(Integer id);

    /**
     * Перевіряє, чи існує користувач з вказаною електронною поштою в базі даних.
     *
     * @param email Електронна пошта для перевірки.
     * @return {@code true}, якщо користувач з такою електронною поштою існує, інакше {@code false}.
     */
    boolean existsByEmail(String email);

    /**
     * Оновлює пароль користувача з вказаною електронною поштою.
     * Використовує власний SQL-запит.
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операцій оновлення.
     *
     * @param email Електронна пошта користувача, пароль якого потрібно оновити.
     * @param password Новий пароль користувача.
     */
    @Modifying
    @Transactional
    @Query("update User u set u.userPassword = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    /**
     * Оновлює дату та час останньої зміни пароля для користувача з вказаною електронною поштою.
     * Використовує власний SQL-запит.
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операцій оновлення.
     *
     * @param email Електронна пошта користувача, для якого потрібно оновити час зміни пароля.
     * @param date  Нова дата та час останньої зміни пароля.
     */
    @Modifying
    @Transactional
    @Query("update User u set u.lastPasswordChanged = ?2 where u.email = ?1")
    void updatePasswordChangingData(String email, LocalDateTime date);

    /**
     * Знаходить користувача за його токеном верифікації.
     * Використовує власний SQL-запит.
     *
     * @param token Токен верифікації користувача.
     * @return Знайдений об'єкт {@link User} або {@code null}, якщо користувача з таким токеном не знайдено.
     */
    @Query("select u from User u where u.verificationToken = ?1")
    User findByVerificationToken(String token);

    /**
     * Встановлює статус користувача як верифікований та видаляє його токен верифікації за наданим токеном.
     * Використовує власний SQL-запит для оновлення.
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операцій оновлення.
     *
     * @param token Токен верифікації облікового запису, який потрібно верифікувати.
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.verified = true, u.verificationToken = NULL WHERE u.verificationToken = ?1")
    void verifyUser(String token);
}
