package oleksandr.jobbit_back.service;

import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.RecruiterProfile;
import oleksandr.jobbit_back.repository.RecruiterProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Value("${jobbit.profile.photo.upload.dir}")
    private String profilePhotoUploadDir;

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

    /**
     * Метод сервісу для збереження фото для рекрутера
     * @param userId ідентифікатор рекрутера
     * @param photo фото, яке треба зберегти
     * @return повний шлях до файлу
     * @throws IOException якщо не вдалося зберегти файл
     */
    public String saveProfilePhoto(Integer userId, MultipartFile photo) throws IOException {
        if (photo == null || photo.isEmpty()) {
            return null;
        }
        String fileName = String.format("recr-%d-profile.%s", userId, getFileExtension(photo.getOriginalFilename()));
        Path destinationFile = Paths.get(profilePhotoUploadDir, fileName).normalize().toAbsolutePath();
        createDirectories(destinationFile.getParent());
        copyFile(photo, destinationFile);
        return "files/userPhotos/" + fileName;
    }

    /**
     * Метод для отримання розширення файлу
     * @param filename повне ім'я файлу
     * @return рядок з розширенням
     */
    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1))
                .orElse("");
    }

    /**
     * Метод, що створює директорію, якщо вона відсутня
     * @param directory шлях до директорії
     * @throws IOException якщо не вдалося виконати метод
     */
    private void createDirectories(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    /**
     * Метод, який копіює отриманий файл у призначену директорію
     * @param file файл, який копіюється
     * @param destinationFile шлях, куди копіювати файл
     * @throws IOException якщо не вдалося скопіювати файл
     */
    private void copyFile(MultipartFile file, Path destinationFile) throws IOException {
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
