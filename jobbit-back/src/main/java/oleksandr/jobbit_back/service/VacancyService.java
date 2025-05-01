package oleksandr.jobbit_back.service;

import oleksandr.jobbit_back.entity.Vacancy;
import oleksandr.jobbit_back.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервіс {@code VacancyService} відповідає за бізнес-логіку, пов'язану з вакансіями, включаючи відображення,
 * додавання, редагування та видалення.
 * Використовує репозиторії {@link VacancyRepository}, для взаємодії з базою даних.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс, що робить його доступним для впровадження залежностей.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    /**
     * Конструктор класу {@code VacancyService}, що ініціалізує залежності.
     *
     * @param vacancyRepository репозиторій для роботи з вакансіями.
     */
    @Autowired
    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    /**
     * Метод для отримання усіх наявних вакансій
     * @return список усіх вакансій
     */
    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    /**
     * Метод для отримання вакансій певного рекрутера
     * @param recruiterId ідентифікатор рекрутера
     * @return список вакансій рекрутера
     */
    public List<Vacancy> getVacanciesByRecruiterId(Integer recruiterId) {
        return vacancyRepository.findByRecruiterProfile_Id(recruiterId);
    }

    /**
     * Метод для отримання певної вакансії
     * @param vacancyId ідентифікатор вакансії
     * @return вакансія
     */
    public Vacancy getVacancyById(Integer vacancyId) {
        return vacancyRepository.findVacancyByVacId(vacancyId);
    }
}
