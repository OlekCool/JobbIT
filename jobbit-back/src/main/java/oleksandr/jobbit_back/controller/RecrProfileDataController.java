package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.service.RecruiterProfileService;
import oleksandr.jobbit_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private final UserService userService;

    /**
     * Конструктор класу {@code RecrProfileDataController}, що ініціалізує необхідні залежності.
     *
     * @param recruiterProfileService сервіс для роботи з профілями рекрутера. Автоматично впроваджується Spring.
     * @param userService сервіс для роботи з користувачами. Автоматично впроваджується Spring.
     */
    @Autowired
    public RecrProfileDataController(RecruiterProfileService recruiterProfileService, UserService userService) {
        this.recruiterProfileService = recruiterProfileService;
        this.userService = userService;
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
     * GET метод для отримання шляху до фото користувача за ID рекрутера.
     * @param userId ID користувача-рекрутера.
     * @return ResponseEntity зі статусом 200 та шляхом до фото, або 404, якщо користувача не знайдено.
     */
    @GetMapping("/profile/{userId}/photo")
    public ResponseEntity<String> getUserPhoto(@PathVariable Integer userId) {
        RecruiterProfile profile = recruiterProfileService.findByUserId(userId);
        if (profile != null) {
            User user = userService.findUserById(profile.getUser().getId());
            if (user != null && user.getPhotoSrc() != null) {
                return ResponseEntity.ok(user.getPhotoSrc());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("files/userPhotos/userDemo.png");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профіль рекрутера з ID " + userId + " не знайдено.");
        }
    }

    /**
     * POST метод для оновлення даних профілю рекрутера
     * @param profile профіль рекрутера, дані якого треба оновити
     * @return ResponseEntity зі статусом 200, якщо успішно оновлено
     */
    @PostMapping("/profile/{userId}/update")
    public ResponseEntity<String> updateRecruiterProfile(@RequestBody RecruiterProfile profile, @PathVariable Integer userId) {
        recruiterProfileService.save(profile);
        return ResponseEntity.ok("Профіль оновлено");
    }

    /**
     * Метод для обробки завантаження фото для рекрутера
     * @param userId ідентифікатор користувача
     * @param photo фото для збереження
     * @return {@link ResponseEntity} зі статусом OK, якщо фото успішно оброблено
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження користувача, якому треба встановити фото
     * Повертає статус INTERNAL_SERVER_ERROR при помилці оброблення фото
     */
    @PostMapping("/profile/{userId}/uploadphoto")
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable Integer userId, @RequestParam("photo") MultipartFile photo) {
        try {
            String filePath = recruiterProfileService.saveProfilePhoto(userId, photo);
            User user = userService.findUserById(userId);
            if (user != null) {
                user.setPhotoSrc(filePath);
                userService.save(user);
                return ResponseEntity.ok("Фото профілю успішно завантажено та збережено за шляхом: " + filePath);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Профіль рекрутера з ID " + userId + " не знайдено.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Не вдалося завантажити фото профілю: " + e.getMessage());
        }
    }
}
