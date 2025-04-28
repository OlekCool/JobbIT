package oleksandr.jobbit_back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Контролер для перевірки існування файлів користувачів
 */
@RestController
@RequestMapping("/api/users")
public class FileExistanceCheckController {

    @Value("${jobbit.profile.photo.upload.dir}") // Шлях до директорії з фото
    private String userPhotoDirectory;

    @GetMapping("/{userId}/profile-photo-url")
    public ResponseEntity<Map<String, String>> getProfilePhotoUrl(@PathVariable Integer userId) {
        String filename = String.format("cand-%d-profile.jpg", userId);
        Path filePath = Paths.get(userPhotoDirectory, filename);
        Map<String, String> response = new HashMap<>();

        if (Files.exists(filePath)) {
            response.put("url", String.format(filename));
        } else {
            String defaultPhotoName = "userDemo.png";
            response.put("url", String.format(defaultPhotoName));
        }

        return ResponseEntity.ok(response);
    }
}
