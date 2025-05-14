package oleksandr.jobbit_back.service;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.dto.MailBody;
import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.User;
import oleksandr.jobbit_back.entity.Vacancy;
import oleksandr.jobbit_back.repository.NotificationRepository;
import oleksandr.jobbit_back.entity.Notification;
import oleksandr.jobbit_back.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервіс {@code NotificationService} відповідає за роботу зі сповіщеннями.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CandidateProfileService candidateProfileService;
    private final VacancyRepository vacancyRepository;
    private final EmailService emailService;
    private final UserService userService;

    /**
     * Конструктор класу {@code EmailService}, що ініціалізує залежності.
     * @param notificationRepository репозиторій для роботи зі сповіщеннями
     * @param candidateProfileService сервіс з логікою роботи з профілем кандидата
     * @param vacancyRepository репозиторій для роботи з вакансіями
     * @param emailService сервіс для роботи з email-повідомленнями
     * @param userService сервіс для роботи з користувачами (отримати email)
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               CandidateProfileService candidateProfileService, VacancyRepository vacancyRepository,
                               EmailService emailService, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.candidateProfileService = candidateProfileService;
        this.vacancyRepository = vacancyRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    /**
     * Отримує всі сповіщення для конкретного кандидата.
     * @param userId Ідентифікатор кандидата.
     * @return Список сповіщень.
     */
    public List<Notification> getAllNotificationsForCandidate(Integer userId) {
        return notificationRepository.findByCandidateProfile_Id(userId);
    }

    /**
     * Видаляє сповіщення за його ID.
     * @param notificationId Ідентифікатор сповіщення для видалення.
     */
    @Modifying
    @Transactional
    public void deleteNotification(Integer notificationId) {
        notificationRepository.deleteByNotId(notificationId);
    }

    /**
     * Видаляє всі сповіщення для конкретного кандидата.
     * @param userId Ідентифікатор кандидата, чиї сповіщення потрібно видалити.
     */
    @Modifying
    @Transactional
    public void deleteAllNotificationsForCandidate(Integer userId) {
        notificationRepository.deleteAllByCandidateId(userId);
    }

    /**
     * Створює, зберігає сповіщення та надсилає email кандидату про рішення щодо вакансії.
     * @param candidateId Ідентифікатор кандидата.
     * @param vacId Ідентифікатор вакансії.
     * @param text Текст сповіщення.
     * @param isAccepted Статус рішення (true - прийнято, false - відхилено).
     */
    @Modifying
    @Transactional
    public void createAndSendVacancyNotification(Integer candidateId, Integer vacId, String text, boolean isAccepted) {
        CandidateProfile candidateProfile = candidateProfileService.findByUserId(candidateId);
        Vacancy vacancy = vacancyRepository.findVacancyByVacId(vacId);

        if (candidateProfile != null && vacancy != null) {
            String subject = "JobbIT: Результат розгляду вашої кандидатури на вакансію \"" + vacancy.getTitle() + "\"";
            String status = isAccepted ? "Запрошено на співбесіду" : "Відхилено";
            String vacancyLink = "http://localhost:8081/candidate-dash/applied";

            String emailText = "Шановний(а) " + candidateProfile.getFirstName() + " " + candidateProfile.getLastName() + ",\n\n" +
                    "Повідомляємо щодо вашої кандидатури на вакансію \"" + vacancy.getTitle() + "\" на платформі JobbIT.\n\n" +
                    "Статус вашої заявки: " + status + ".\n\n" +
                    "Повідомлення від рекрутера: " + text + "\n\n" +
                    "Ви можете знайти деталі вакансії у вакансіях, на які ви відгукнулися: " + vacancyLink + "\n\n" +
                    "З повагою,\nКоманда JobbIT";

            Notification notification = new Notification();
            notification.setCandidateProfile(candidateProfile);
            notification.setVacId(vacId);
            notification.setText(text);
            notification.setNotifDate(LocalDateTime.now());
            notificationRepository.save(notification);

            User userForReceiving = userService.findUserById(candidateId);

            if (userForReceiving != null) {
                MailBody mailBody = new MailBody();
                mailBody.setTo(userForReceiving.getEmail());
                mailBody.setSubject(subject);
                mailBody.setText(emailText);

                emailService.sendSimpleMessage(mailBody);
            }
        } else {
            System.err.println("Не знайдено профіль кандидата з ID: " + candidateId + " або вакансію з ID: " + vacId);
        }
    }

    /**
     * Метод для створення Notification та надсилання email-повідомлення у випадку зміни деталей вакансії для кандидатів,
     * що відгукнулися
     * @param candidateId ідентифікатор кандидата
     * @param vacId ідентифікатор вакансії
     */
    @Modifying
    @Transactional
    public void createAndSendVacancyChangeMessage(Integer candidateId, Integer vacId) {
        CandidateProfile candidateProfile = candidateProfileService.findByUserId(candidateId);
        Vacancy vacancy = vacancyRepository.findVacancyByVacId(vacId);

        if (candidateProfile != null && vacancy != null) {
            String subject = "JobbIT: Зміна умов вакансії, на яку ви відгукнулися: \"" + vacancy.getTitle() + "\"";
            String vacancyLink = "http://54.211.79.181/candidate-dash/applied";
            String emailText = "Шановний(а) " + candidateProfile.getFirstName() + " " + candidateProfile.getLastName() + ",\n\n" +
                    "Повідомляємо про те, що були змінені умови вакансії \"" + vacancy.getTitle() + "\" на платформі JobbIT.\n\n" +
                    "Ви можете знайти деталі вакансії у вакансіях, на які ви відгукнулися: " + vacancyLink + "\n\n" +
                    "З повагою,\nКоманда JobbIT";

            Notification notification = new Notification();
            notification.setCandidateProfile(candidateProfile);
            notification.setVacId(vacId);
            notification.setText("Умови вакансії '" + vacancy.getTitle() + "' було змінено");
            notification.setNotifDate(LocalDateTime.now());
            notificationRepository.save(notification);

            User userForReceiving = userService.findUserById(candidateId);

            if (userForReceiving != null) {
                MailBody mailBody = new MailBody();
                mailBody.setTo(userForReceiving.getEmail());
                mailBody.setSubject(subject);
                mailBody.setText(emailText);

                emailService.sendSimpleMessage(mailBody);
            }
        } else {
            System.err.println("Не знайдено профіль кандидата з ID: " + candidateId + " або вакансію з ID: " + vacId);
        }
    }
}
