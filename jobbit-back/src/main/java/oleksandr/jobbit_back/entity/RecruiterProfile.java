package oleksandr.jobbit_back.entity;

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
@Table(name = "recr_profile ")
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
    private User user;

    /**
     * Додаткові дані профілю рекрутера.
     */
    @Column(name = "some_data")
    private String someData;

    /**
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про профіль рекрутера.
     * Включає ідентифікатор профілю та email пов'язаного користувача (якщо він існує).
     *
     * @return Рядкова репрезентація об'єкта {@code RecruiterProfile}.
     */
    @Override
    public String toString() {
        return "RecruiterProfile{"
                + "id=" + id
                + ", user=" + (user != null ? user.getEmail() : "no user")
                + '}';
    }
}
