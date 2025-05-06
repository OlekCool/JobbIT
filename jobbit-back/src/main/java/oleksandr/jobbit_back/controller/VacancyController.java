package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.Vacancy;
import oleksandr.jobbit_back.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для операцій, пов'язаних з вакансіями.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    /**
     * Конструктор класу {@code VacancyController}, що ініціалізує необхідні залежності.
     *
     * @param vacancyService Сервіс для роботи з вакансіями. Автоматично впроваджується Spring.
     */
    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    /**
     * Метод контролера для отримання всіх вакансій
     * @return {@link ResponseEntity} зі статусом 200 та вакансіями
     */
    @GetMapping("/all")
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        List<Vacancy> vacancies = vacancyService.getAllVacancies();
        return ResponseEntity.ok(vacancies);
    }

    /**
     * Метод контролера для отримання всіх вакансій рекрутера
     * @return {@link ResponseEntity} зі статусом 200 та вакансіями рекрутера
     */
    @GetMapping("/recr-vacancies")
    public ResponseEntity<List<Vacancy>> getRecruiterVacancies(@RequestParam Integer recruiterId) {
        List<Vacancy> recr_vacancies = vacancyService.getVacanciesByRecruiterId(recruiterId);
        return ResponseEntity.ok(recr_vacancies);
    }

    /**
     * Метод контролера для видачі списку кандидатів, що відгукнулися на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @return @return {@link ResponseEntity} зі статусом 200 та кандидатами
     */
    @GetMapping("/recr-vacancies/{vacancyId}/applicants")
    public ResponseEntity<List<CandidateProfile>> getApplicantsForVacancy(@PathVariable Integer vacancyId) {
        List<CandidateProfile> applicants = vacancyService.getApplicantsByVacancyId(vacancyId);
        return ResponseEntity.ok(applicants);
    }

    /**
     * Метод контролера для додавання нової вакансії рекрутером
     * @param recruiterId ідентифікатор рекрутера, який додає вакансію
     * @param newVacancy дані нової вакансії у тілі запиту
     * @return {@link ResponseEntity} зі статусом 201 (Created) та створеною вакансією
     */
    @PostMapping("/recr-vacancies/{recruiterId}/add")
    public ResponseEntity<Vacancy> addNewVacancy(@PathVariable Integer recruiterId, @RequestBody Vacancy newVacancy) {
        Vacancy savedVacancy = vacancyService.addNewVacancy(recruiterId, newVacancy);
        return new ResponseEntity<>(savedVacancy, HttpStatus.CREATED);
    }

    /**
     * Метод контролера для редагування існуючої вакансії
     * @param id ідентифікатор вакансії
     * @param editedVacancy редагована вакансія
     * @return {@link ResponseEntity} зі статусом 200 та редагованою вакансією
     */
    @PostMapping("/recr-vacancies/editvacancy/{id}")
    public ResponseEntity<Vacancy> editVacancy(@PathVariable Integer id, @RequestBody Vacancy editedVacancy) {
        Vacancy updatedVacancy = vacancyService.updateVacancy(id, editedVacancy);
        if (updatedVacancy != null) {
            return ResponseEntity.ok(updatedVacancy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Метод контролера для видалення вакансії
     * @param id ідентифікатор вакансії
     * @return {@link ResponseEntity} зі статусом 204 (noContent)
     */
    @PostMapping("/recr-vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод контролера для отримання збережених кандидатом вакансій
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200, а також вакансіями
     */
    @GetMapping("/cand-vacancies/saved")
    public ResponseEntity<List<Vacancy>> getSavedVacancies(@RequestParam Integer candidateId) {
        List<Vacancy> savedVacancies = vacancyService.getSavedVacanciesForCandidate(candidateId);
        return ResponseEntity.ok(savedVacancies);
    }

    /**
     * Метод контролера для отримання поданих відгуків кандидатом на вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200, а також вакансіями
     */
    @GetMapping("/cand-vacancies/applied")
    public ResponseEntity<List<Vacancy>> getAppliedVacancies(@RequestParam Integer candidateId) {
        List<Vacancy> appliedVacancies = vacancyService.getAppliedVacanciesForCandidate(candidateId);
        return ResponseEntity.ok(appliedVacancies);
    }

    /**
     * Метод контролера для збереження вакансії кандидатом
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 201
     */
    @PostMapping("/cand-vacancies/save/{vacancyId}")
    public ResponseEntity<Void> saveVacancy(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        System.out.println("Викликано saveVacancy для кандидата: " + candidateId + ", вакансії: " + vacancyId);
        vacancyService.saveVacancy(candidateId, vacancyId);
        System.out.println("Збереження вакансії " + vacancyId + " для кандидата " + candidateId + " виконано.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Метод контролера для перевірки, чи вакансія збережена
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200, та булевим значенням
     */
    @GetMapping("/cand-vacancies/is_saved/{vacancyId}")
    public ResponseEntity<Boolean> isVacancySaved(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        boolean isSaved = vacancyService.isVacancySaved(candidateId, vacancyId);
        return ResponseEntity.ok(isSaved);
    }

    /**
     * Метод контролера для видалення вакансії зі збережених
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 204
     */
    @PostMapping("/cand-vacancies/unsave/{vacancyId}")
    public ResponseEntity<Void> unsaveVacancy(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        vacancyService.unsaveVacancy(candidateId, vacancyId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод контролера для відгуку на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 201
     */
    @PostMapping("/cand-vacancies/apply/{vacancyId}")
    public ResponseEntity<Void> applyVacancy(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        vacancyService.applyVacancy(candidateId, vacancyId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Метод контролера для перевірки, чи відгукувався кандидат
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200, та булевим значенням
     */
    @GetMapping("/cand-vacancies/is_applied/{vacancyId}")
    public ResponseEntity<Boolean> isVacancyApplied(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        boolean isApplied = vacancyService.isVacancyApplied(candidateId, vacancyId);
        return ResponseEntity.ok(isApplied);
    }

    /**
     * Метод для видалення відгуку на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 204
     */
    @PostMapping("/cand-vacancies/unapply/{vacancyId}")
    public ResponseEntity<Void> unapplyVacancy(@PathVariable Integer vacancyId, @RequestParam Integer candidateId) {
        vacancyService.unapplyVacancy(candidateId, vacancyId);
        return ResponseEntity.noContent().build();
    }

}
