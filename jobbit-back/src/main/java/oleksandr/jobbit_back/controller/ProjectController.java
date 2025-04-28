package oleksandr.jobbit_back.controller;

import oleksandr.jobbit_back.entity.Project;
import oleksandr.jobbit_back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST-контролер для операцій з проєктами саме кандидата (тобто, тільки кандидат виконує це)
 *
 * @author Oleksandr Borovyk
 */
@RestController
@RequestMapping("/api/candidate-dash/profile/{userId}")
@PreAuthorize("hasRole('CANDIDATE')")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Конструктор класу {@code ProjectController}, що ініціалізує залежності {@link ProjectService}
     *
     * @param projectService Сервіс для роботи з користувачами
     */
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Метод контролера для знаходження конкретного проєкту за ідентифікатором
     * @param id ідентифікатор проєкту
     * @param userId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішне отримання проєкту
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження проєкту
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id, @PathVariable Integer userId) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Метод контролера для додавання нового проєкту кандидатом
     * @param project проєкт для додавання
     * @param userId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 201 (CREATED), або статус INTERNAL_SERVER_ERROR, якщо не
     * вдалося зберегти фото
     */
    @PostMapping("/addproject")
    public ResponseEntity<Project> addProject(@PathVariable Integer userId, @RequestBody Project project, @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            if (photo != null && !photo.isEmpty()) {
                String filePath = projectService.saveProjectPhoto(userId, project.getProjId(), photo);
                project.setProjPictSrc(filePath);
            }
            Project createdProject = projectService.addProject(userId, project);
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Метод контролера для редагування існуючого проєкту
     * @param id ідентифікатор проєкту
     * @param updatedProject оновлені дані проєкту
     * @param userId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200 (OK) та повідомленням про успішне оновлення, або статус
     * INTERNAL_SERVER_ERROR, якщо не вдалося зберегти фото
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження проєкту, якого треба редагувати
     */
    @PostMapping("/editproject/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody Project updatedProject, @PathVariable Integer userId, @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            if (photo != null && !photo.isEmpty()) {
                String filePath = projectService.saveProjectPhoto(userId, id, photo);
                updatedProject.setProjPictSrc(filePath);
            }
            updatedProject.setProjId(id);
            Project project = projectService.updateProject(id, updatedProject);
            return ResponseEntity.ok(project);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Метод контролера для видалення проєкту
     * @param id ідентифікатор проєкту
     * @return {@link ResponseEntity} зі статусом noContent
     * Повертає статус 404 (NOT_FOUND) у разі не знаходження проєкту, якого треба видалити
     */
    @PostMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id, @PathVariable Integer userId) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Метод контролера для повернення списку усіх проєктів кандидата
     * @param userId ідентифікатор кандидата
     * @return {@link ResponseEntity} зі статусом 200
     */
    @GetMapping("/")
    public ResponseEntity<List<Project>> getProjectsByCandidateId(@PathVariable Integer userId) {
        List<Project> projects = projectService.getProjectsByCandidateId(userId);
        return ResponseEntity.ok(projects);
    }
}
