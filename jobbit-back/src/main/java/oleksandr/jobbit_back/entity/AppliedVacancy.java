package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code AppliedVacancy} представляє запис того, який кандидат яку вакансію зберіг.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "applied_vacancies")
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

    public AppliedVacancy() {

    }

    public AppliedVacancy(CandidateProfile candidateProfile, Vacancy vacancy) {
        this.id = new AppliedVacancyId(candidateProfile.getId(), vacancy.getVacId());
        this.candidateProfile = candidateProfile;
        this.vacancy = vacancy;
    }

    public AppliedVacancyId getId() {
        return id;
    }

    public void setId(AppliedVacancyId id) {
        this.id = id;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public CandidateProfile getCandidateProfile() {
        return candidateProfile;
    }

    public void setCandidateProfile(CandidateProfile candidateProfile) {
        this.candidateProfile = candidateProfile;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }
}
