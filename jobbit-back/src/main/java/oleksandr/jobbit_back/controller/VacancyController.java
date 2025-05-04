package oleksandr.jobbit_back.controller;

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
    @GetMapping("/my-vacancies")
    public ResponseEntity<List<Vacancy>> getRecruiterVacancies(@RequestParam Integer recruiterId) {
        List<Vacancy> recr_vacancies = vacancyService.getVacanciesByRecruiterId(recruiterId);
        return ResponseEntity.ok(recr_vacancies);
    }

    /**
     * Метод контролера для додавання нової вакансії рекрутером
     * @param recruiterId ідентифікатор рекрутера, який додає вакансію
     * @param newVacancy дані нової вакансії у тілі запиту
     * @return {@link ResponseEntity} зі статусом 201 (Created) та створеною вакансією
     */
    @PostMapping("/my-vacancies/{recruiterId}/add")
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
    @PostMapping("/my-vacancies/editvacancy/{id}")
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
    @PostMapping("/my-vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }
}
