package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій {@code VacancyRepository} надає інтерфейс для взаємодії з базою даних для сутності {@link Vacancy}.
 * Він розширює {@link JpaRepository}, надаючи стандартні операції CRUD (створення, читання, оновлення, видалення)
 * для сутності {@code Vacancy}, а також містить власні методи для виконання специфічних запитів.
 * Анотація {@code @Repository} позначає цей інтерфейс як Spring Data JPA репозиторій, що дозволяє Spring
 * автоматично знаходити та керувати ним.
 *
 * @author Oleksandr Borovyk
 */
@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {

    /**
     * Метод для отримання вакансій певного рекрутера
     * @param recruiterId ідентифікатор рекрутера
     * @return список вакансій рекрутера
     */
    List<Vacancy> findByRecruiterProfile_Id(Integer recruiterId);

    /**
     * Отримання інформації про вакансію
     * @param vacId ідентифікатор вакансії
     * @return одна конкретна вакансія
     */
    Vacancy findVacancyByVacId(Integer vacId);


}
