package oleksandr.jobbit_back.service;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.CandidateProfile;
import oleksandr.jobbit_back.entity.Project;
import oleksandr.jobbit_back.repository.CandidateProfileRepository;
import oleksandr.jobbit_back.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервіс {@code ProjectService} відповідає за бізнес-логіку роботи з проєктами кандидата.
 * Анотація {@code @Service} позначає цей клас як Spring-сервіс.
 *
 * @author Oleksandr Borovyk
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    @Value("${jobbit.project.photo.upload.dir}")
    private String uploadDir;

    /**
     * Конструктор класу {@code ProjectService}, що ініціалізує залежності.
     *
     * @param projectRepository Репозиторій для роботи з проєктами. Автоматично впроваджується Spring.
     * @param candidateProfileRepository Репозиторій для роботи з профілями кандидатів. Автоматично впроваджується Spring.
     */
    @Autowired
    public ProjectService(ProjectRepository projectRepository, CandidateProfileRepository candidateProfileRepository) {
        this.projectRepository = projectRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    /**
     * Метод для отримання списку проєктів кандидата
     * @param candidateId ідентифікатор профілю кандидата
     * @return список проєктів
     */
    public List<Project> getProjectsByCandidateId(Integer candidateId) {
        return projectRepository.findByCandidateProfileId(candidateId);
    }

    /**
     * Метод для отримання одного проєкту
     * @param id ідентифікатор проєкту
     * @return проєкт
     */
    public Project getProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Проєкт з ID " + id + " не знайдено"));
    }

    /**
     * Метод для додавання нового проєкту для певного кандидата
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операції додавання.
     * @param candidateId ідентифікатор кандидата
     * @param project проєкт, який треба додати
     * @return результат збереження проєкту
     */
    @Modifying
    @Transactional
    public Project addProject(Integer candidateId, Project project) {
        CandidateProfile candidateProfile = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new NoSuchElementException("Профіль кандидата з ID " + candidateId + " не знайдено"));
        project.setCandidateProfile(candidateProfile);
        return projectRepository.save(project);
    }

    /**
     * Метод для редагування наявного проєкту певного кандидата
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операції оновлення.
     * @param id ідентифікатор проєкту
     * @param updatedProject дані оновленого проєкту
     * @return результат збереження проєкту
     */
    @Modifying
    @Transactional
    public Project updateProject(Integer id, Project updatedProject) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Проєкт з ID " + id + " не знайдено"));

        existingProject.setProjName(updatedProject.getProjName());
        existingProject.setProjDescription(updatedProject.getProjDescription());
        existingProject.setProjGithubLink(updatedProject.getProjGithubLink());
        existingProject.setProjPictSrc(updatedProject.getProjPictSrc());

        return projectRepository.save(existingProject);
    }

    /**
     * Метод для видалення проєкту
     * Анотації {@code @Modifying} та {@code @Transactional} необхідні для виконання операції видалення.
     * @param id ідентифікатор певного проєкту, якого треба видалити
     */
    @Modifying
    @Transactional
    public void deleteProject(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Проєкт з ID " + id + " не знайдено");
        }
        projectRepository.deleteById(id);
    }

    /**
     * Метод для збереження фото проєкту
     * @param userId ідентифікатор кандидата
     * @param projectId ідентифікатор проєкту
     * @param photo файл, який передається від кандидата
     * @return рядок повного шляху до файлу разом з файлом
     * @throws IOException якщо помилка під час збереження файлу
     */
    public String saveProjectPhoto(Integer userId, Integer projectId, MultipartFile photo) throws IOException {
        if (photo == null || photo.isEmpty()) {
            return null;
        }

        String fileName = String.format("cand-%d-project-%d.%s", userId, projectId, getFileExtension(photo.getOriginalFilename()));
        Path destinationFile = Paths.get(uploadDir, fileName)
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().toFile().exists()) {
            Files.createDirectories(destinationFile.getParent());
        }

        try (var inputStream = photo.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return "JobbIT/files/candidateProjectPhotos/" + fileName;
        } catch (IOException e) {
            throw new IOException("Не вдалося зберегти файл " + fileName, e);
        }
    }

    /**
     * Метод для отримання розширення отриманого файлу
     * @param filename ім'я файлу
     * @return рядок з розширенням файлу
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

}
