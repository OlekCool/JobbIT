package oleksandr.jobbit_back.service;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.repository.CandidateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервіс {@code CandidateProfileService} відповідає за роботу з профілями кандидатів.
 * Поки що пустий.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    /**
     * Конструктор класу {@code CandidateProfileService}, що ініціалізує залежності.
     *
     * @param candidateProfileRepository Репозиторій для роботи з даними профілів.
     * Автоматично впроваджується Spring.
     */
    @Autowired
    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
    }

    /**
     * Знаходить профіль кандидата за ID користувача.
     * @param userId ID користувача.
     * @return Об'єкт профілю або null, якщо не знайдено.
     */
    public CandidateProfile findByUserId(Integer userId) {
        Optional<CandidateProfile> optionalProfile = candidateProfileRepository.findByUserId(userId);
        return optionalProfile.orElse(null);
    }

    /**
     * Зберігає або оновлює профіль кандидата.
     * @param profile Профіль для збереження.
     * @return Збережений профіль.
     */
    public CandidateProfile save(CandidateProfile profile) {
        return candidateProfileRepository.save(profile);
    }
}
