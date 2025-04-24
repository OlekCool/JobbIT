package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;

import lombok.Data;

/**
 * Entity-клас {@code CandidateProfile} представляє профіль кандидата, пов'язаний з обліковим записом
 * користувача ({@link User}). Містить додаткову інформацію, специфічну для кандидатів. Використовує
 * бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "cand_profile")
@Data
public class CandidateProfile {

    /**
     * Ідентифікатор профілю кандидата. Збігається з ідентифікатором пов'язаного користувача ({@link User}).
     */
    @Id
    @Column(name = "cand_id")
    private Integer id;

    /**
     * Зв'язок "один до одного" з таблицею {@link User}.
     * Анотація `@MapsId` вказує, що первинний ключ цього entity збігається з первинним ключем пов'язаного entity {@code User}.
     * Зовнішній ключ `id` вказує на таблицю `users`.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    /**
     * Ім'я кандидата
     */
    @Column(name = "firstname")
    private String firstName;

    /**
     * Прізвище кандидата
     */
    @Column(name = "lastname")
    private String lastName;

    /**
     * По батькові кандидата
     */
    @Column(name = "fathername")
    private String fatherName;

    /**
     * Вік кандидата
     */
    @Column(name = "years_old")
    private Integer yearsOld;

    /**
     * Країна кандидата
     */
    @Column(name = "country")
    private String country;

    /**
     * Місто кандидата
     */
    @Column(name = "city")
    private String city;

    /**
     * Досвід кандидата у роках
     */
    @Column(name = "experience_years")
    private Integer experienceYears;

    /**
     * Рівень англійської кандидата
     */
    @Column(name = "level_engl")
    private String levelEng;

    /**
     * Бажана посада для кандидата
     */
    @Column(name = "job_wants")
    private String jobWants;

    /**
     * Бажана зарплата для кандидата
     */
    @Column(name = "salary_wants")
    private Integer salaryWants;

    /**
     * Телеграм нік кандидата
     */
    @Column(name = "tg_nick")
    private String tgNick;

    /**
     * Телефон кандидата
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Github акаунт кандидата
     */
    @Column(name = "github")
    private String github;

    /**
     * Шлях до резюме-файлу кандидата кандидата
     */
    @Column(name = "cv_src")
    private String cvPath;

    /**
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про профіль кандидата.
     * Включає ідентифікатор профілю та email пов'язаного користувача (якщо він існує).
     *
     * @return Рядкова репрезентація об'єкта {@code CandidateProfile}.
     */
    @Override
    public String toString() {
        return "CandidateProfile{" +
                "id=" + id +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", yearsOld=" + yearsOld +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", experienceYears=" + experienceYears +
                ", levelEng='" + levelEng + '\'' +
                ", jobWants='" + jobWants + '\'' +
                ", salaryWants='" + salaryWants + '\'' +
                ", tgNick='" + tgNick + '\'' +
                ", phone='" + phone + '\'' +
                ", github='" + github + '\'' +
                ", cvPath='" + cvPath + '\'' +
                '}';
    }
}
