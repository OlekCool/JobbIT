package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity-клас {@code RecruiterProfile} представляє профіль рекрутера, пов'язаний з обліковим записом
 * користувача ({@link User}). Містить додаткову інформацію, специфічну для рекрутерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "recr_profile")
public class RecruiterProfile {

    /**
     * Ідентифікатор профілю рекрутера. Відповідає ідентифікатору пов'язаного користувача ({@link User}).
     */
    @Id
    @Column(name = "recr_id")
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
     * Ім'я рекрутера
     */
    @Column(name = "firstname")
    private String firstName;

    /**
     * Прізвище рекрутера
     */
    @Column(name = "lastname")
    private String lastName;

    /**
     * По батькові рекрутера
     */
    @Column(name = "fathername")
    private String fatherName;

    /**
     * Компанія рекрутера
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * Е.Пошта рекрутера
     */
    @Column(name = "email")
    private String email;

    /**
     * Телефон рекрутера
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Інстаграм рекрутера
     */
    @Column(name = "instagram")
    private String instagram;

    /**
     * Країна рекрутера
     */
    @Column(name = "country")
    private String country;

    /**
     * Місто рекрутера
     */
    @Column(name = "city")
    private String city;

    /**
     * Опис про рекрутера
     */
    @Column(name = "description")
    private String description;

    /**
     * Прив'язка вакансій до рекрутера зі зв'язком один до багатьох. Каскадні операції є.
     */
    @OneToMany(mappedBy = "recruiterProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vacancy> vacancies = new ArrayList<>();

    public RecruiterProfile() {
    }

    public RecruiterProfile(Integer id, User user, String firstName, String lastName, String fatherName,
                            String companyName, String email, String phone, String instagram, String country,
                            String city, String description, List<Vacancy> vacancies) {
        this.id = id;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.instagram = instagram;
        this.country = country;
        this.city = city;
        this.description = description;
        this.vacancies = vacancies;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "id=" + id +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", instagram='" + instagram + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
