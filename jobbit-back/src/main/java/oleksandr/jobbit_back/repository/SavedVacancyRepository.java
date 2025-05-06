package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.SavedVacancy;
import oleksandr.jobbit_back.entity.SavedVacancyId;
import oleksandr.jobbit_back.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій {@code SavedVacancyRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link SavedVacancy}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code SavedVacancy}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface SavedVacancyRepository extends JpaRepository<SavedVacancy, SavedVacancyId> {

    /**
     * Метод репозиторію для знаходження запису за ідентифікатором кандидата та вакансії
     * @param whoSaved ідентифікатор, хто зберіг вакансію
     * @param vacId ідентифікатор вакансії
     * @return список збережених записів
     */
    Optional<SavedVacancy> findById_WhoSavedAndId_VacId(Integer whoSaved, Integer vacId);

    /**
     * Метод репозиторію для знаходження всіх записів про збережені вакансії кандидата. Кастомний запит
     * @param candidateId ідентифікатор кандидата
     * @return список вакансій
     */
    @Query("SELECT sv.vacancy from SavedVacancy sv WHERE sv.candidateProfile.id = :candidateId")
    List<Vacancy> findSavedVacanciesByCandidateId(@Param("candidateId") Integer candidateId);


}
