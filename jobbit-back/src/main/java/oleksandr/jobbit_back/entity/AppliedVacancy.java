package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code AppliedVacancy} представляє запис того, який кандидат яку вакансію зберіг
 * Використовує бібліотеку Lombok для автогенерації конструкторів, гетерів та сетерів.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "applied_vacancies")
@Data
@Getter
@Setter
public class AppliedVacancy {

    /**
     * Складений первинний ключ, який складається з ідентифікаторів кандидата та вакансії
     */
    @EmbeddedId
    private AppliedVacancyId id;

    /**
     * Дата, коли вакансію збережено
     */
    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    /**
     * Чи відгук прийнято
     */
    @Column(name = "is_accepted")
    private Boolean isAccepted;

    /**
     * Зв'язок багато до одного з кандидатом
     */
    @ManyToOne
    @MapsId("whoApplied")
    @JoinColumn(name = "who_applied")
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
    public AppliedVacancy() {

    }

    /**
     * Конструктор з записуванням інформації, хто зберіг і яку вакансію
     * @param candidateProfile профіль кандидата
     * @param vacancy вакансія
     */
    public AppliedVacancy(CandidateProfile candidateProfile, Vacancy vacancy) {
        this.id = new AppliedVacancyId(candidateProfile.getId(), vacancy.getVacId());
        this.candidateProfile = candidateProfile;
        this.vacancy = vacancy;
    }

}
