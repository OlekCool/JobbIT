package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.service.CandidateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контролер {@code CandProfileDataController} надає певні операції по роботі з профілями кандидатів
 * Використовує бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/candidate-dash")
public class CandProfileDataController {

    private final CandidateProfileService candidateProfileService;

    /**
     * Конструктор класу {@code CandProfileDataController}, що ініціалізує необхідні залежності.
     *
     * @param candidateProfileService сервіс для роботи з профілями кандидата. Автоматично впроваджується Spring.
     */
    @Autowired
    public CandProfileDataController(CandidateProfileService candidateProfileService) {
        this.candidateProfileService = candidateProfileService;
    }

    /**
     * Метод GET, який знаходить і повертає профіль певного кандидата, знаючи його id
     * @param userId id користувача
     * @return ResponseEntity зі статусом 200, якщо успішно, інакше 404 (профілю не знайдено)
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<CandidateProfile> getProfileData(@PathVariable Integer userId) {
        CandidateProfile profile = candidateProfileService.findByUserId(userId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * POST метод для оновлення даних профілю користувача
     * @param profile профіль кандидата, дані якого треба оновити
     * @return ResponseEntity зі статусом 200, якщо успішно оновлено
     */
    @PostMapping("/profile/update")
    public ResponseEntity<String> updateCandidateProfile(@RequestBody CandidateProfile profile) {
        candidateProfileService.save(profile);
        return ResponseEntity.ok("Профіль оновлено");
    }

}
