package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій {@code CandidateProfileRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link CandidateProfile}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code CandidateProfile}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Integer> {

    /**
     * Пошук профілю кандидата за ID користувача. Використовує власний запит на знаходження профілю за айді-користувача
     * @param userId ID користувача.
     * @return Optional з профілем кандидата.
     */
    @Query("SELECT cp FROM CandidateProfile cp WHERE cp.user.id = :userId")
    Optional<CandidateProfile> findByUserId(@Param("userId") Integer userId);

}
