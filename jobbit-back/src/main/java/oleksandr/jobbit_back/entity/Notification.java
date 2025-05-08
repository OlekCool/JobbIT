package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * Entity-клас {@code Notification} представляє сповіщення щодо вакансій, на які кандидат відгукнувся в базі даних.
 * Містить інформацію про те, якому кандидату надійшло сповіщення, щодо якої вакансії це сповіщення, текст сповіщення
 * Також має зв'язок з entity {@link CandidateProfile}, щоб призначити сповіщення для певного кандидата).
 * Використовує Lombok.
 *
 * @author Oleksadnr Borovyk
 */
@Entity
@Table(name = "cand_notifications")
@Data
public class Notification {

    /**
     * Ідентифікатор сповіщення. Автоматична генерація базою даних
     */
    @Id
    @Column(name = "not_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notId;

    /**
     * Профіль кандидата, якому прийшло сповіщення. Зв'язок багато до одного
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private CandidateProfile candidateProfile;

    /**
     * Ідентифікатор вакансії, щодо якої прийшло сповіщення
     */
    @Column(name = "vac_id")
    private Integer vacId;

    /**
     * Текст сповіщення щодо вакансії
     */
    @Column(name = "text")
    private String text;

    /**
     * Дата та час надходження сповіщення
     */
    @Column(name = "notif_date")
    private LocalDateTime notifDate;

}
