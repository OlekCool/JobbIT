package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій {@code AppliedVacancyRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link AppliedVacancy}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code AppliedVacancy}. Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA
 * репозиторій, що дозволяє Spring автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface AppliedVacancyRepository extends JpaRepository<AppliedVacancy, AppliedVacancyId> {

    /**
     * Метод репозиторію для знаходження запису за ідентифікатором кандидата та вакансії
     * @param whoSaved ідентифікатор, хто відгукнувся на вакансію
     * @param vacId ідентифікатор вакансії
     * @return список збережених записів
     */
    Optional<AppliedVacancy> findById_WhoAppliedAndId_VacId(Integer whoSaved, Integer vacId);

    /**
     * Метод репозиторію для знаходження всіх кандидатів, які відгукнулися на певну вакансію. Кастомний запит
     * @param vacancyId ідентифікатор вакансії
     * @return список профілів кандидатів
     */
    @Query("SELECT av.candidateProfile FROM AppliedVacancy av WHERE av.vacancy.vacId = :vacancyId")
    List<CandidateProfile> findCandidatesByVacancyId(@Param("vacancyId") Integer vacancyId);

    /**
     * Метод репозиторію для знаходження вакансій, на які кандидат відгукнувся
     * @param candidateId ідентифікатор кандидата
     * @return список вакансій
     */
    @Query("SELECT av.vacancy FROM AppliedVacancy av WHERE av.candidateProfile.id = :candidateId")
    List<Vacancy> findAppliedVacanciesByCandidateId(@Param("candidateId") Integer candidateId);

    /**
     * Метод репозиторію для зміни статусу на "прийнято" з запису щодо поданих відгуків на вакансію
     * @param candidateId ідентифікатор кандидата
     * @param vacId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    @Query("UPDATE AppliedVacancy av SET av.isAccepted = true WHERE av.candidateProfile.id = :candidateId and av.vacancy.vacId = :vacId")
    void acceptCandidate(@Param("candidateId") Integer candidateId, @Param("vacId") Integer vacId);


    /**
     * Метод репозиторію для зміни статусу на "прийнято" з запису щодо поданих відгуків на вакансію
     * @param candidateId ідентифікатор кандидата
     * @param vacId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    @Query("UPDATE AppliedVacancy av SET av.isAccepted = false WHERE av.candidateProfile.id = :candidateId and av.vacancy.vacId = :vacId")
    void denyCandidate(@Param("candidateId") Integer candidateId, @Param("vacId") Integer vacId);

}
