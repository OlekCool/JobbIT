package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity-клас {@code User} представляє користувача в базі даних.
 * Містить основну інформацію про користувача, таку як email, пароль, роль, дату реєстрації,
 * статус верифікації та токен верифікації. Також має зв'язки з іншими entity ({@link ForgotPassword},
 * {@link CandidateProfile}, {@link RecruiterProfile}).
 *
 * @author Oleksadnr Borovyk
 */
@Entity
@Table(name = "users")
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
     * Рядок, яккий містить посилання на фото користувача
     */
    @Column(name = "photo_src")
    private String photoSrc;

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
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CandidateProfile candidateProfile;

    /**
     * Зв'язок "один до одного" з сутністю {@link RecruiterProfile}.
     * Містить профіль рекрутера, якщо користувач має відповідну роль.
     * Всі операції каскадні.
     */
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RecruiterProfile recruiterProfile;

    public User() {
    }

    public User(Integer id, String email, String userPassword, LocalDate registrationDate, Role userRole,
                LocalDateTime lastPasswordChanged, Boolean verified, String verificationToken, String photoSrc,
                ForgotPassword forgotPassword, CandidateProfile candidateProfile, RecruiterProfile recruiterProfile) {
        this.id = id;
        this.email = email;
        this.userPassword = userPassword;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
        this.lastPasswordChanged = lastPasswordChanged;
        this.verified = verified;
        this.verificationToken = verificationToken;
        this.photoSrc = photoSrc;
        this.forgotPassword = forgotPassword;
        this.candidateProfile = candidateProfile;
        this.recruiterProfile = recruiterProfile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getLastPasswordChanged() {
        return lastPasswordChanged;
    }

    public void setLastPasswordChanged(LocalDateTime lastPasswordChanged) {
        this.lastPasswordChanged = lastPasswordChanged;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public ForgotPassword getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(ForgotPassword forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public CandidateProfile getCandidateProfile() {
        return candidateProfile;
    }

    public void setCandidateProfile(CandidateProfile candidateProfile) {
        this.candidateProfile = candidateProfile;
    }

    public RecruiterProfile getRecruiterProfile() {
        return recruiterProfile;
    }

    public void setRecruiterProfile(RecruiterProfile recruiterProfile) {
        this.recruiterProfile = recruiterProfile;
    }

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
