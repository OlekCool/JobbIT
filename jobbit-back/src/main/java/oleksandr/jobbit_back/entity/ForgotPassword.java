package oleksandr.jobbit_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;

import lombok.Data;

import java.util.Date;

/**
 * Entity-клас {@code ForgotPassword} представляє інформацію, пов'язану з процесом відновлення забутого пароля користувача.
 * Зберігає одноразовий пароль (OTP), термін його дії та посилання на відповідного користувача ({@link User}).
 * Використовує Lombok.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "forgot_password")
@Data
public class ForgotPassword {

    /**
     * Унікальний ідентифікатор запису про відновлення пароля. Автоматично генерується базою даних.
     */
    @Id
    @Column(name = "fpid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    /**
     * Одноразовий пароль (OTP), надісланий користувачеві для підтвердження запиту на відновлення пароля.
     * Не може бути null.
     */
    @Column(name = "otp", nullable = false)
    private Integer otp;

    /**
     * Дата та час, до якого OTP є дійсним. Після цієї дати OTP вважається недійсним.
     * Не може бути null.
     */
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    /**
     * Зв'язок "один до одного" з сутністю {@link User}.
     * Вказує на користувача, якому належить цей запис про відновлення пароля.
     * Використовується зовнішній ключ `user_id`.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
