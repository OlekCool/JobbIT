package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entity-клас {@code Project} представляє проєкт в базі даних.
 * Містить основну інформацію про проєкт, таку як назва, опис, посилання на github та картинку.
 * Також має зв'язок з entity {@link CandidateProfile}, щоб призначити проєкт на певного кандидата).
 *
 * @author Oleksadnr Borovyk
 */
@Entity
@Table(name = "projects")
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

    public Project() {
    }

    public Project(Integer projId, CandidateProfile candidateProfile, String projName, String projDescription, String projPictSrc, String projGithubLink) {
        this.projId = projId;
        this.candidateProfile = candidateProfile;
        this.projName = projName;
        this.projDescription = projDescription;
        this.projPictSrc = projPictSrc;
        this.projGithubLink = projGithubLink;
    }

    public Integer getProjId() {
        return projId;
    }

    public void setProjId(Integer projId) {
        this.projId = projId;
    }

    public CandidateProfile getCandidateProfile() {
        return candidateProfile;
    }

    public void setCandidateProfile(CandidateProfile candidateProfile) {
        this.candidateProfile = candidateProfile;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjDescription() {
        return projDescription;
    }

    public void setProjDescription(String projDescription) {
        this.projDescription = projDescription;
    }

    public String getProjPictSrc() {
        return projPictSrc;
    }

    public void setProjPictSrc(String projPictSrc) {
        this.projPictSrc = projPictSrc;
    }

    public String getProjGithubLink() {
        return projGithubLink;
    }

    public void setProjGithubLink(String projGithubLink) {
        this.projGithubLink = projGithubLink;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projId=" + projId +
                ", candidateProfile=" + candidateProfile +
                ", projName='" + projName + '\'' +
                ", projDescription='" + projDescription + '\'' +
                ", projPictSrc='" + projPictSrc + '\'' +
                ", projGithubLink='" + projGithubLink + '\'' +
                '}';
    }
}
