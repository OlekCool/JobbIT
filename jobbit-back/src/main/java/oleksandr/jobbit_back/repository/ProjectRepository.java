package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій {@code ProjectRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link Project}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code Project}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * Метод репозиторію, призначений для отримання списку проєктів певного кандидата. Використовує кастомний запит
     * @param profileId id профілю
     * @return список проєктів
     */
    @Query("select proj from Project proj where proj.candidateProfile.id = ?1")
    List<Project> findByCandidateProfileId(Integer profileId);

    /**
     * Метод репозиторію, призначений для отримання певного проєкту за його ідентифікатором
     * @param id id проєкту
     * @return один проєкт
     */
    @Query("select proj from Project proj where proj.projId = ?1")
    Optional<Project> findById(Integer id);

}
