package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity-клас {@code AppliedVacancy} представляє запис того, який кандидат на яку вакансію відгукнувся.
 *
 * @author Oleksandr Borovyk
 */
@Entity
@Table(name = "liked_vacancies")
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

    public SavedVacancy() {

    }

    public SavedVacancy(CandidateProfile candidateProfile, Vacancy vacancy) {
        this.id = new SavedVacancyId(candidateProfile.getId(), vacancy.getVacId());
        this.candidateProfile = candidateProfile;
        this.vacancy = vacancy;
    }

    public SavedVacancyId getId() {
        return id;
    }

    public void setId(SavedVacancyId id) {
        this.id = id;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
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
