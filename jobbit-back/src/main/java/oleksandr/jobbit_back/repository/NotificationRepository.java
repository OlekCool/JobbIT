package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій {@code NotificationRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link oleksandr.jobbit_back.entity.Notification}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code Notification}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * Отримує всі сповіщення для конкретного кандидата за його ID
     * @param userId Ідентифікатор кандидата.
     * @return Список сповіщень для даного кандидата.
     */
    List<Notification> findByCandidateProfile_Id(Integer userId);

    /**
     * Видаляє сповіщення за його ID.
     * @param notificationId Ідентифікатор сповіщення, яке потрібно видалити.
     */
    void deleteByNotId(Integer notificationId);

    /**
     * Видаляє всі сповіщення для конкретного кандидата за його ID.
     * @param userId Ідентифікатор кандидата, всі сповіщення якого потрібно видалити.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.candidateProfile.id = :userId")
    void deleteAllByCandidateId(@Param("userId") Integer userId);

}
