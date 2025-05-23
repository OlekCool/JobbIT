package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity-клас {@code CandidateProfile} представляє профіль кандидата, пов'язаний з обліковим записом
 * користувача ({@link User}). Містить додаткову інформацію, специфічну для кандидатів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "cand_profile")
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
     * Проєкти певного кандидата
     */
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Project> projects = new ArrayList<>();

    /**
     * Сповіщення певного кандидата
     */
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();

    public CandidateProfile() {
    }

    public CandidateProfile(Integer id, User user, String firstName, String lastName, String fatherName,
                            Integer yearsOld, String country, String city, Integer experienceYears,
                            String levelEng, String jobWants, Integer salaryWants, String tgNick, String phone,
                            String github, String cvPath, List<Project> projects, List<Notification> notifications) {
        this.id = id;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.yearsOld = yearsOld;
        this.country = country;
        this.city = city;
        this.experienceYears = experienceYears;
        this.levelEng = levelEng;
        this.jobWants = jobWants;
        this.salaryWants = salaryWants;
        this.tgNick = tgNick;
        this.phone = phone;
        this.github = github;
        this.cvPath = cvPath;
        this.projects = projects;
        this.notifications = notifications;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Integer getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(Integer yearsOld) {
        this.yearsOld = yearsOld;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLevelEng() {
        return levelEng;
    }

    public void setLevelEng(String levelEng) {
        this.levelEng = levelEng;
    }

    public String getJobWants() {
        return jobWants;
    }

    public void setJobWants(String jobWants) {
        this.jobWants = jobWants;
    }

    public Integer getSalaryWants() {
        return salaryWants;
    }

    public void setSalaryWants(Integer salaryWants) {
        this.salaryWants = salaryWants;
    }

    public String getTgNick() {
        return tgNick;
    }

    public void setTgNick(String tgNick) {
        this.tgNick = tgNick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

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
