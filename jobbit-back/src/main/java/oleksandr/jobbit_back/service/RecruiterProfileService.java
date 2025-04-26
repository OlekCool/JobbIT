package oleksandr.jobbit_back.service;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.repository.RecruiterProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервіс {@code RecruiterProfileService} відповідає за роботу з профілями рекрутерів.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;

    /**
     * Конструктор класу {@code RecruiterProfileService}, що ініціалізує залежності.
     *
     * @param recruiterProfileRepository Репозиторій для роботи з даними профілів.
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    /**
     * Знаходить профіль рекрутера за ID користувача.
     * @param userId ID користувача.
     * @return Об'єкт профілю або null, якщо не знайдено.
     */
    public RecruiterProfile findByUserId(Integer userId) {
        Optional<RecruiterProfile> optionalProfile = recruiterProfileRepository.findByUserId(userId);
        return optionalProfile.orElse(null);
    }

    /**
     * Зберігає або оновлює профіль рекрутера.
     * @param profile Профіль для збереження.
     * @return Збережений профіль.
     */
    public RecruiterProfile save(RecruiterProfile profile) {
        return recruiterProfileRepository.save(profile);
    }
}
