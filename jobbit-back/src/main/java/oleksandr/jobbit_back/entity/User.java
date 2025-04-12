package oleksandr.jobbit_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity-клас {@code User} представляє користувача в базі даних.
 * Містить основну інформацію про користувача, таку як email, пароль, роль, дату реєстрації,
 * статус верифікації та токен верифікації. Також має зв'язки з іншими entity ({@link ForgotPassword},
 * {@link CandidateProfile}, {@link RecruiterProfile}). Використовує Lombok.
 *
 * @author Oleksadnr Borovyk
 */
@Entity
@Table(name = "users")
@Data
public class User {

    /**
     * Унікальний ідентифікатор користувача. Автоматично генерується базою даних.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Електронна пошта користувача. Повинна бути унікальною в системі та відповідати формату email.
     * Поле не може бути пустим.
     */
    @Column(name = "email", unique = true)
    @NotBlank(message = "The email can't be empty")
    @Email(message = "Enter a valid email")
    private String email;

    /**
     * Закодований пароль користувача. Поле не може бути пустим.
     */
    @Column(name = "user_password")
    @NotBlank(message = "The password can't be empty")
    private String userPassword;

    /**
     * Дата реєстрації користувача в системі.
     */
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    /**
     * Роль користувача в системі ({@link Role}). Визначає права та рівень доступу.
     */
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    /**
     * Дата та час останньої зміни пароля користувача.
     */
    @Column(name = "last_password_changed")
    private LocalDateTime lastPasswordChanged;

    /**
     * Статус верифікації облікового запису користувача. {@code true}, якщо верифіковано, інакше {@code false}.
     */
    @Column(name = "verified")
    private Boolean verified;

    /**
     * Токен, що використовується для верифікації електронної пошти користувача.
     * Стає {@code null} після успішної верифікації.
     */
    @Column(name = "verification_token")
    private String verificationToken;

    /**
     * Зв'язок "один до одного" з сутністю {@link ForgotPassword}.
     * Використовується для зберігання інформації про процес відновлення пароля користувача.
     * Каскадне видалення та видалення orphan-записів налаштовано.
     */
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
    private ForgotPassword forgotPassword;

    /**
     * Зв'язок "один до одного" з сутністю {@link CandidateProfile}.
     * Містить профіль кандидата, якщо користувач має відповідну роль.
     * Всі операції каскадні.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CandidateProfile candidateProfile;

    /**
     * Зв'язок "один до одного" з сутністю {@link RecruiterProfile}.
     * Містить профіль рекрутера, якщо користувач має відповідну роль.
     * Всі операції каскадні.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RecruiterProfile recruiterProfile;

    /**
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про користувача.
     * Включає email, дату реєстрації, роль та статус верифікації.
     *
     * @return Рядкова репрезентація об'єкта {@code User}.
     */
    @Override
    public String toString() {
        return "User{"
                + "email='" + email + '\''
                + ", registrationDate=" + registrationDate
                + ", userRole=" + userRole
                + ", verified=" + verified
                + '}';
    }
}
