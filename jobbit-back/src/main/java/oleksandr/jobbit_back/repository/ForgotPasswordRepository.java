package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.ForgotPassword;
import oleksandr.jobbit_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій {@code ForgotPasswordRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link ForgotPassword}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD, а також містить власні методи
 * для пошуку записів відновлення пароля за OTP та користувачем, а також для видалення запису за ID.
 * Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA репозиторій.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    /**
     * Знаходить запис про відновлення пароля ({@link ForgotPassword}) за наданим одноразовим паролем (OTP)
     * та пов'язаним користувачем ({@link User}).
     * Повертає {@link Optional<ForgotPassword>}, який містить знайдений запис або є порожнім, якщо такого запису не знайдено.
     *
     * @param otp  Одноразовий пароль (OTP) для пошуку.
     * @param user Користувач ({@link User}), для якого шукається запис.
     * @return {@link Optional<ForgotPassword>} з знайденим записом або порожній {@link Optional}.
     */
    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    /**
     * Видаляє запис про відновлення пароля ({@link ForgotPassword}) за його унікальним ідентифікатором (FPID).
     * Анотації {@code @Transactional} та {@code @Modifying} необхідні для виконання операцій видалення за
     * допомогою власного запиту.
     *
     * @param id Унікальний ідентифікатор (FPID) запису, який потрібно видалити.
     */
    @Transactional
    @Modifying
    @Query("delete from ForgotPassword fp where fp.fpid = ?1")
    void deleteById(Integer id);
}
