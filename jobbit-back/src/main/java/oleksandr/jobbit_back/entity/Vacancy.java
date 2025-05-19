package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code Vacancy} представляє вакансію, пов'язану з певним профілем рекрутера ({@link RecruiterProfile}),
 * який виставив її. Містить інформацію, необхідну для прийняття рішення про відгук на вакансію.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "vacancies")
@JsonIgnoreProperties("recruiterProfile")
public class Vacancy {

    /**
     * Унікальний ідентифікатор вакансії. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vac_id")
    private Integer vacId;

    /**
     * Унікальний ідентифікатор рекрутера. Береться з профілю рекрутера. Зв'язок багато до одного.
     */
    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false)
    @JsonBackReference
    private RecruiterProfile recruiterProfile;

    /**
     * Назва вакансії.
     */
    @Column(name = "title")
    private String title;

    /**
     * Опис вакансії.
     */
    @Column(name = "description")
    private String description;

    /**
     * Віддалена, частково-віддалена, офіс.
     */
    @Column(name = "remote")
    private String remote;

    /**
     * Вакансія фултайм/парт-тайм.
     */
    @Column(name = "fulltime")
    private Boolean fulltime;

    /**
     * Необхідний рівень англ.мови.
     */
    @Column(name = "level_eng")
    private String levelEng;

    /**
     * Мінімальний досвід у роках.
     */
    @Column(name = "min_exp")
    private Integer minExp;

    /**
     * Встановлена зарплата на вакансію.
     */
    @Column(name = "set_salary")
    private Integer setSalary;

    /**
     * Дата розміщення вакансії.
     */
    @Column(name = "posted_date")
    private LocalDateTime postedDate;

    public Vacancy() {
    }

    public Vacancy(Integer vacId, RecruiterProfile recruiterProfile, String title, String description, String remote,
                   Boolean fulltime, String levelEng, Integer minExp, Integer setSalary, LocalDateTime postedDate) {
        this.vacId = vacId;
        this.recruiterProfile = recruiterProfile;
        this.title = title;
        this.description = description;
        this.remote = remote;
        this.fulltime = fulltime;
        this.levelEng = levelEng;
        this.minExp = minExp;
        this.setSalary = setSalary;
        this.postedDate = postedDate;
    }

    public Integer getVacId() {
        return vacId;
    }

    public void setVacId(Integer vacId) {
        this.vacId = vacId;
    }

    public RecruiterProfile getRecruiterProfile() {
        return recruiterProfile;
    }

    public void setRecruiterProfile(RecruiterProfile recruiterProfile) {
        this.recruiterProfile = recruiterProfile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public Boolean getFulltime() {
        return fulltime;
    }

    public void setFulltime(Boolean fulltime) {
        this.fulltime = fulltime;
    }

    public String getLevelEng() {
        return levelEng;
    }

    public void setLevelEng(String levelEng) {
        this.levelEng = levelEng;
    }

    public Integer getMinExp() {
        return minExp;
    }

    public void setMinExp(Integer minExp) {
        this.minExp = minExp;
    }

    public Integer getSetSalary() {
        return setSalary;
    }

    public void setSetSalary(Integer setSalary) {
        this.setSalary = setSalary;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "vacId=" + vacId +
                ", postedBy=" + recruiterProfile +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", remote='" + remote + '\'' +
                ", fulltime=" + fulltime +
                ", levelEng='" + levelEng + '\'' +
                ", minExp=" + minExp +
                ", setSalary=" + setSalary +
                ", postedDate=" + postedDate +
                '}';
    }
}
