package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code AppliedVacancy} представляє запис того, який кандидат на яку вакансію відгукнувся
 * Використовує бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "liked_vacancies")
@Data
@Getter
@Setter
public class SavedVacancy {

    /**
     * Складений первинний ключ, який складається з ідентифікаторів кандидата та вакансії
     */
    @EmbeddedId
    private SavedVacancyId id;

    /**
     * Дата, коли кандидат відгукнувся на вакансію
     */
    @Column(name = "like_date")
    private LocalDateTime saveDate;

    /**
     * Зв'язок багато до одного з кандидатом
     */
    @ManyToOne
    @MapsId("whoSaved")
    @JoinColumn(name = "who_saved")
    private CandidateProfile candidateProfile;

    /**
     * Зв'язок багато до одного з вакансією
     */
    @ManyToOne
    @MapsId("vacId")
    @JoinColumn(name = "vac_id")
    private Vacancy vacancy;

    /**
     * Пустий конструктор
     */
    public SavedVacancy() {

    }

    /**
     * Конструктор з записуванням інформації, хто на яку вакансію відгукнувся
     * @param candidateProfile профіль кандидата
     * @param vacancy вакансія
     */
    public SavedVacancy(CandidateProfile candidateProfile, Vacancy vacancy) {
        this.id = new SavedVacancyId(candidateProfile.getId(), vacancy.getVacId());
        this.candidateProfile = candidateProfile;
        this.vacancy = vacancy;
    }

    @Override
    public String toString() {
        return "SavedVacancy{" +
                "id=" + id +
                ", saveDate=" + saveDate +
                ", candidateProfile=" + candidateProfile +
                ", vacancy=" + vacancy +
                '}';
    }
}
