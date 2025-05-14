package oleksandr.jobbit_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
    @Column(name = "text", length = 1000)
    private String text;

    /**
     * Дата та час надходження сповіщення
     */
    @Column(name = "notif_date")
    private LocalDateTime notifDate;

    public Notification() {
    }

    public Notification(Integer notId, CandidateProfile candidateProfile, Integer vacId, String text, LocalDateTime notifDate) {
        this.notId = notId;
        this.candidateProfile = candidateProfile;
        this.vacId = vacId;
        this.text = text;
        this.notifDate = notifDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notId=" + notId +
                ", candidateProfile=" + candidateProfile +
                ", vacId=" + vacId +
                ", text='" + text + '\'' +
                ", notifDate=" + notifDate +
                '}';
    }
}
