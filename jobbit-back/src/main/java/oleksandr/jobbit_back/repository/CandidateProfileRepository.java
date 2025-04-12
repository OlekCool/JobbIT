package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
