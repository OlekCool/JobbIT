package oleksandr.jobbit_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * Клас для збереження складеного первинного ключа в БД
 */
@Embeddable
public class AppliedVacancyId implements Serializable {

    /**
     * Ідентифікатор кандидата, який відгукнувся
     */
    @Column(name = "who_applied")
    private Integer whoApplied;

    /**
     * Ідентифікатор вакансію, яку збережено
     */
    @Column(name = "vac_id")
    private Integer vacId;

    public AppliedVacancyId() {

    }

    public AppliedVacancyId(Integer whoApplied, Integer vacId) {
        this.whoApplied = whoApplied;
        this.vacId = vacId;
    }

    public Integer getWhoApplied() {
        return whoApplied;
    }

    public void setWhoApplied(Integer whoApplied) {
        this.whoApplied = whoApplied;
    }

    public Integer getVacId() {
        return vacId;
    }

    public void setVacId(Integer vacId) {
        this.vacId = vacId;
    }
}
