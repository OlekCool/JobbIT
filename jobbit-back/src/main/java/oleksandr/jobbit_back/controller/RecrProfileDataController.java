package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контролер {@code RecrProfileDataController} надає певні операції по роботі з профілями рекрутерів
 * Використовує бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/recruiter-dash")
public class RecrProfileDataController {

    private final RecruiterProfileService recruiterProfileService;

    /**
     * Конструктор класу {@code RecrProfileDataController}, що ініціалізує необхідні залежності.
     *
     * @param recruiterProfileService сервіс для роботи з профілями рекрутера. Автоматично впроваджується Spring.
     */
    @Autowired
    public RecrProfileDataController(RecruiterProfileService recruiterProfileService) {
        this.recruiterProfileService = recruiterProfileService;
    }

    /**
     * Метод GET, який знаходить і повертає профіль певного рекрутера, знаючи його id
     * @param userId id користувача
     * @return ResponseEntity зі статусом 200, якщо успішно, інакше 404 (профілю не знайдено)
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<RecruiterProfile> getProfileData(@PathVariable Integer userId) {
        RecruiterProfile profile = recruiterProfileService.findByUserId(userId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * POST метод для оновлення даних профілю рекрутера
     * @param profile профіль рекрутера, дані якого треба оновити
     * @return ResponseEntity зі статусом 200, якщо успішно оновлено
     */
    @PostMapping("/profile/update")
    public ResponseEntity<String> updateRecruiterProfile(@RequestBody RecruiterProfile profile) {
        recruiterProfileService.save(profile);
        return ResponseEntity.ok("Профіль оновлено");
    }

}
