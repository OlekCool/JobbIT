package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.service.CandidateProfileService;
import oleksandr.jobbit_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private final UserService userService;

    /**
     * Конструктор класу {@code CandProfileDataController}, що ініціалізує необхідні залежності.
     *
     * @param candidateProfileService сервіс для роботи з профілями кандидата. Автоматично впроваджується Spring.
     * @param userService сервіс для роботи з користувачами. Автоматично впроваджується Spring.
     */
    @Autowired
    public CandProfileDataController(CandidateProfileService candidateProfileService, UserService userService) {
        this.candidateProfileService = candidateProfileService;
        this.userService = userService;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET метод для отримання шляху до фото користувача за ID кандидата.
     * @param userId ID користувача-кандидата.
     * @return ResponseEntity зі статусом 200 та шляхом до фото, або 404, якщо користувача не знайдено.
     */
    @GetMapping("/profile/{userId}/photo")
    public ResponseEntity<String> getUserPhoto(@PathVariable Integer userId) {
        CandidateProfile profile = candidateProfileService.findByUserId(userId);
        if (profile != null) {
            User user = userService.findUserById(profile.getUser().getId());
            if (user != null && user.getPhotoSrc() != null) {
                return ResponseEntity.ok(user.getPhotoSrc());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("files/userPhotos/userDemo.png");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профіль кандидата з ID " + userId + " не знайдено.");
        }
    }

    /**
     * POST метод для оновлення даних профілю користувача
     * @param profile профіль кандидата, дані якого треба оновити
     * @return ResponseEntity зі статусом 200, якщо успішно оновлено
     */
    @PostMapping("/profile/{userId}/update")
    public ResponseEntity<String> updateCandidateProfile(@RequestBody CandidateProfile profile, @PathVariable Integer userId) {
        candidateProfileService.save(profile);
        return ResponseEntity.ok("Профіль оновлено");
    }

    /**
     * Метод для обробки завантаження фото для кандидата
     * @param userId ідентифікатор користувача
     * @param photo фото для збереження
     * @return {@link ResponseEntity} зі статусом OK, якщо фото успішно оброблено
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження користувача, якому треба встановити фото
     * Повертає статус INTERNAL_SERVER_ERROR при помилці оброблення фото
     */
    @PostMapping("/profile/{userId}/uploadphoto")
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable Integer userId, @RequestParam("photo") MultipartFile photo) {
        try {
            String filePath = candidateProfileService.saveProfilePhoto(userId, photo);
            User user = userService.findUserById(userId);
            if (user != null) {
                user.setPhotoSrc(filePath);
                userService.save(user);
                return ResponseEntity.ok("Фото профілю успішно завантажено та збережено за шляхом: " + filePath);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профіль кандидата з ID " + userId + " не знайдено.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не вдалося завантажити фото профілю: " + e.getMessage());
        }
    }

    /**
     * Метод для встановлення резюме для кандидата
     * @param userId ідентифікатор кандидата
     * @param cvFile файл з резюме
     * @return {@link ResponseEntity} зі статусом OK, якщо резюме успішно оброблено
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження кандидата, якому треба встановити резюме
     * Повертає статус INTERNAL_SERVER_ERROR при помилці оброблення файлу резюме
     */
    @PostMapping("/profile/{userId}/uploadcv")
    public ResponseEntity<String> uploadCvFile(@PathVariable Integer userId, @RequestParam("cvFile") MultipartFile cvFile) {
        try {
            String filePath = candidateProfileService.saveCvFile(userId, cvFile);
            CandidateProfile profile = candidateProfileService.findByUserId(userId);
            if (profile != null) {
                profile.setCvPath(filePath);
                candidateProfileService.save(profile);
                return ResponseEntity.ok("CV успішно завантажено та збережено за шляхом: " + filePath);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профіль кандидата з ID " + userId + " не знайдено.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не вдалося завантажити CV: " + e.getMessage());
        }
    }
}
