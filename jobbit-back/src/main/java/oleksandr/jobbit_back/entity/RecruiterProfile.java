package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import lombok.Data;

/**
 * Entity-клас {@code RecruiterProfile} представляє профіль рекрутера, пов'язаний з обліковим записом
 * користувача ({@link User}). Містить додаткову інформацію, специфічну для рекрутерів. Використовує
 * бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "recr_profile")
@Data
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
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про профіль рекрутера.
     * Включає ідентифікатор профілю та email пов'язаного користувача (якщо він існує).
     *
     * @return Рядкова репрезентація об'єкта {@code RecruiterProfile}.
     */
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
