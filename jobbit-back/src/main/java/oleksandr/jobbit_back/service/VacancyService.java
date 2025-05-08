package oleksandr.jobbit_back.service;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.*;
import oleksandr.jobbit_back.repository.AppliedVacancyRepository;
import oleksandr.jobbit_back.repository.SavedVacancyRepository;
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
    private final SavedVacancyRepository savedVacancyRepository;
    private final AppliedVacancyRepository appliedVacancyRepository;
    private final RecruiterProfileService recruiterProfileService;
    private final CandidateProfileService candidateProfileService;
    private final NotificationService notificationService;

    /**
     * Конструктор класу {@code VacancyService}, що ініціалізує залежності.
     *
     * @param vacancyRepository репозиторій для роботи з вакансіями.
     * @param recruiterProfileService репозиторій для роботи з профілями рекрутерів.
     * @param appliedVacancyRepository репозиторій для роботи з кандидатами, які відгукнулися.
     * @param savedVacancyRepository репозиторій для роботи з вакансіями, які зберіг кандидат.
     * @param candidateProfileService репозиторій для роботи з профілями кандидатів.
     * @param notificationService сервіс для роботи зі сповіщеннями для кандидата щодо вакансій.
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public VacancyService(VacancyRepository vacancyRepository, SavedVacancyRepository savedVacancyRepository,
                          AppliedVacancyRepository appliedVacancyRepository, RecruiterProfileService recruiterProfileService,
                          CandidateProfileService candidateProfileService, NotificationService notificationService) {
        this.vacancyRepository = vacancyRepository;
        this.savedVacancyRepository = savedVacancyRepository;
        this.appliedVacancyRepository = appliedVacancyRepository;
        this.recruiterProfileService = recruiterProfileService;
        this.candidateProfileService = candidateProfileService;
        this.notificationService = notificationService;
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

            Vacancy savedVacancy = vacancyRepository.save(existingVacancy);

            List<CandidateProfile> applicants = appliedVacancyRepository.findCandidatesByVacancyId(vacId);

            String notificationText = "Дані вакансії \"" + savedVacancy.getTitle() + "\", на яку ви відгукнулися, було змінено.";
            for (CandidateProfile applicant : applicants) {
                notificationService.createAndSendVacancyChangeMessage(applicant.getId(), vacId);
            }

            return savedVacancy;
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

    /**
     * Метод для збереження вакансії кандидатом
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void saveVacancy(Integer candidateId, Integer vacancyId) {
        CandidateProfile candidateProfile = candidateProfileService.findByUserId(candidateId);
        Vacancy vacancy = vacancyRepository.findVacancyByVacId(vacancyId);

        SavedVacancy savedVacancy = new SavedVacancy(candidateProfile, vacancy);
        savedVacancy.setSaveDate(LocalDateTime.now());
        savedVacancyRepository.save(savedVacancy);
    }

    /**
     * Перевірка, чи вакансія збережена кандидатом
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     * @return булеве значення
     */
    public boolean isVacancySaved(Integer candidateId, Integer vacancyId) {
        return savedVacancyRepository.findById_WhoSavedAndId_VacId(candidateId, vacancyId).isPresent();
    }

    /**
     * Метод для скасування збереження вакансії кандидатом
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void unsaveVacancy(Integer candidateId, Integer vacancyId) {
        SavedVacancyId id = new SavedVacancyId(candidateId, vacancyId);
        savedVacancyRepository.deleteById(id);
    }

    /**
     * Метод для відгуку кандидата на вакансію
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void applyVacancy(Integer candidateId, Integer vacancyId) {
        CandidateProfile candidateProfile = candidateProfileService.findByUserId(candidateId);
        Vacancy vacancy = vacancyRepository.findVacancyByVacId(vacancyId);

        AppliedVacancy appliedVacancy = new AppliedVacancy(candidateProfile, vacancy);
        appliedVacancy.setApplyDate(LocalDateTime.now());
        appliedVacancyRepository.save(appliedVacancy);
    }

    /**
     * Метод для перевірки, чи кандидат відгукнувся на вакансію
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     */
    public boolean isVacancyApplied(Integer candidateId, Integer vacancyId) {
        return appliedVacancyRepository.findById_WhoAppliedAndId_VacId(candidateId, vacancyId).isPresent();
    }

    /**
     * Метод для скасування відгуку кандидата на вакансію
     * @param candidateId ідентифікатор кандидата
     * @param vacancyId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void unapplyVacancy(Integer candidateId, Integer vacancyId) {
        AppliedVacancyId id = new AppliedVacancyId(candidateId, vacancyId);
        appliedVacancyRepository.deleteById(id);
    }

    /**
     * Метод для знаходження всіх кандидатів, хто відгукнувся на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @return список кандидатів
     */
    public List<CandidateProfile> getApplicantsByVacancyId(Integer vacancyId) {
        return appliedVacancyRepository.findCandidatesByVacancyId(vacancyId);
    }

    /**
     * Метод для знаходження вакансій, які зберіг кандидат
     * @param candidateId ідентифікатор кандидата
     * @return список вакансій
     */
    public List<Vacancy> getSavedVacanciesForCandidate(Integer candidateId) {
        return savedVacancyRepository.findSavedVacanciesByCandidateId(candidateId);
    }

    /**
     * Метод для знаходження вакансій, на які кандидат відгукнувся
     * @param candidateId ідентифікатор кандидата
     * @return список вакансій
     */
    public List<Vacancy> getAppliedVacanciesForCandidate(Integer candidateId) {
        return appliedVacancyRepository.findAppliedVacanciesByCandidateId(candidateId);
    }

    /**
     * Метод для прийняття кандидатури вакансії
     * @param candidateId ідентифікатор кандидата
     * @param vacId ідентифікатор вакансії
     * @param notificationText текст повідомлення для сповіщення
     */
    @Modifying
    @Transactional
    public void acceptCandidate(Integer candidateId, Integer vacId, String notificationText) {
        appliedVacancyRepository.acceptCandidate(candidateId, vacId);
        notificationService.createAndSendVacancyNotification(candidateId, vacId, notificationText, true);
    }

    /**
     * Метод для відхилення кандидатури вакансії
     * @param candidateId ідентифікатор кандидата
     * @param vacId ідентифікатор вакансії
     * @param notificationText текст повідомлення для сповіщення
     */
    @Modifying
    @Transactional
    public void denyCandidate(Integer candidateId, Integer vacId, String notificationText) {
        appliedVacancyRepository.denyCandidate(candidateId, vacId);
        notificationService.createAndSendVacancyNotification(candidateId, vacId, notificationText, false);
    }

}
