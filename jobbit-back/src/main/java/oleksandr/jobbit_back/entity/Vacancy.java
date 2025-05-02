package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code Vacancy} представляє вакансію, пов'язану з певним профілем рекрутера ({@link RecruiterProfile}),
 * який виставив її. Містить інформацію, необхідну для прийняття рішення про відгук на вакансію.
 * Використовує бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "vacancies")
@Data
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

    /**
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про вакансію.
     * Включає поля, притаманні вакансії
     *
     * @return Рядкова репрезентація об'єкта {@code Vacancy}.
     */
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
