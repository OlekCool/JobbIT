package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.Project;
import oleksandr.jobbit_back.entity.Vacancy;
import oleksandr.jobbit_back.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
