package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій {@code RecruiterProfileRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link RecruiterProfile}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code RecruiterProfile}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {

    /**
     * Пошук профілю рекрутера за ID користувача. Використовує власний запит на знаходження профілю за айді-користувача
     * @param userId ID користувача.
     * @return Optional з профілем рекрутера.
     */
    @Query("SELECT rp FROM RecruiterProfile rp WHERE rp.user.id = :userId")
    Optional<RecruiterProfile> findByUserId(@Param("userId") Integer userId);

}
