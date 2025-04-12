package oleksandr.jobbit_back.entity;

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
@Table(name = "cand_profile ")
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
    private User user;

    /**
     * Додаткові дані профілю кандидата.
     */
    @Column(name = "some_data")
    private String someData;

    /**
     * Перевизначений метод {@code toString()} для зручного виведення основної інформації про профіль кандидата.
     * Включає ідентифікатор профілю та email пов'язаного користувача (якщо він існує).
     *
     * @return Рядкова репрезентація об'єкта {@code CandidateProfile}.
     */
    @Override
    public String toString() {
        return "CandidateProfile{"
                + "id=" + id
                + ", user=" + (user != null ? user.getEmail() : "no user")
                + '}';
    }
}
