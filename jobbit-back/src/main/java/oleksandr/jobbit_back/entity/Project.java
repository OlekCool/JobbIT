package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity-клас {@code Project} представляє проєкт в базі даних.
 * Містить основну інформацію про проєкт, таку як назва, опис, посилання на github та картинку.
 * Також має зв'язок з entity {@link CandidateProfile}, щоб призначити проєкт на певного кандидата).
 * Використовує Lombok.
 *
 * @author Oleksadnr Borovyk
 */
@Entity
@Table(name = "projects")
@Data
public class Project {

    /**
     * Ідентифікатор проєкту. Автоматична генерація базою даних
     */
    @Id
    @Column(name = "proj_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projId;

    /**
     * Профіль кандидата, якому належить проєкт. Зв'язок багато до одного
     */
    @ManyToOne
    @JoinColumn(name = "proj_user", nullable = false)
    @JsonBackReference
    private CandidateProfile candidateProfile;

    /**
     * Ім'я проєкту
     */
    @Column(name = "proj_name")
    private String projName;

    /**
     * Опис проєкту
     */
    @Column(name = "proj_description")
    private String projDescription;

    /**
     * Шлях до фото для проєкту
     */
    @Column(name = "proj_pict_src")
    private String projPictSrc;

    /**
     * Посилання на github проєкту
     */
    @Column(name = "proj_github_link")
    private String projGithubLink;

}
