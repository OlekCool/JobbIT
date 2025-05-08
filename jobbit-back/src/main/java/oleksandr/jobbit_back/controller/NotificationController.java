package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.Notification;
import oleksandr.jobbit_back.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для операцій, пов'язаних зі сповіщеннями кандидата
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Конструктор класу {@code NotificationController}, що ініціалізує необхідні залежності.
     * @param notificationService Сервіс для роботи зі сповіщеннями.
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    /**
     * Метод контролера для отримання всіх сповіщень для конкретного кандидата.\
     * @param candidateId Ідентифікатор кандидата.
     * @return {@link ResponseEntity} зі статусом 200 та списком сповіщень.
     */
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<Notification>> getAllNotificationsForCandidate(@PathVariable Integer candidateId) {
        List<Notification> notifications = notificationService.getAllNotificationsForCandidate(candidateId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Метод контролера для видалення сповіщення за його ID.
     * @param notificationId Ідентифікатор сповіщення для видалення.
     * @return {@link ResponseEntity} зі статусом 204 (No Content).
     */
    @PostMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод контролера для видалення всіх сповіщень для конкретного кандидата.
     * @param candidateId Ідентифікатор кандидата.
     * @return {@link ResponseEntity} зі статусом 204 (No Content).
     */
    @PostMapping("/candidate/{candidateId}/all")
    public ResponseEntity<Void> deleteAllNotificationsForCandidate(@PathVariable Integer candidateId) {
        notificationService.deleteAllNotificationsForCandidate(candidateId);
        return ResponseEntity.noContent().build();
    }
}
