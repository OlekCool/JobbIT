package oleksandr.jobbit_back.service;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.entity.Vacancy;
import oleksandr.jobbit_back.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final RecruiterProfileService recruiterProfileService;

    /**
     * Конструктор класу {@code VacancyService}, що ініціалізує залежності.
     *
     * @param vacancyRepository репозиторій для роботи з вакансіями.
     * @param recruiterProfileService репозиторій для роботи з профілями рекрутерів.
     */
    @Autowired
    public VacancyService(VacancyRepository vacancyRepository, RecruiterProfileService recruiterProfileService) {
        this.vacancyRepository = vacancyRepository;
        this.recruiterProfileService = recruiterProfileService;
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

    /**
     * Метод для додавання нової вакансії рекрутером
     * @param recruiterId ідентифікатор рекрутера
     * @param vacancy вакансія для додавання
     * @return результат збереження вакансії у БД
     */
    @Modifying
    @Transactional
    public Vacancy addNewVacancy(Integer recruiterId, Vacancy vacancy) {
        RecruiterProfile recruiterProfile = recruiterProfileService.findByUserId(recruiterId);
        vacancy.setRecruiterProfile(recruiterProfile);
        vacancy.setPostedDate(LocalDateTime.now());
        return vacancyRepository.save(vacancy);
    }

    /**
     * Метод для редагування існуючої вакансії
     * @param vacId ідентифікатор вакансії
     * @param updatedVacancy оновлені дані вакансії
     * @return результат збереження
     */
    @Modifying
    @Transactional
    public Vacancy updateVacancy(Integer vacId, Vacancy updatedVacancy) {
        Vacancy existingVacancy = vacancyRepository.findVacancyByVacId(vacId);

        if (existingVacancy != null) {
            existingVacancy.setTitle(updatedVacancy.getTitle());
            existingVacancy.setDescription(updatedVacancy.getDescription());
            existingVacancy.setRemote(updatedVacancy.getRemote());
            existingVacancy.setFulltime(updatedVacancy.getFulltime());
            existingVacancy.setLevelEng(updatedVacancy.getLevelEng());
            existingVacancy.setMinExp(updatedVacancy.getMinExp());
            existingVacancy.setSetSalary(updatedVacancy.getSetSalary());
            existingVacancy.setPostedDate(LocalDateTime.now());

            return vacancyRepository.save(existingVacancy);
        } else {
            return null;
        }
    }

    /**
     * Видалення вакансії за її id
     * @param id ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void deleteVacancy(Integer id) {
        vacancyRepository.deleteById(id);
    }
}
