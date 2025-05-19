package oleksandr.jobbit_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * Клас для збереження складеного первинного ключа в БД
 */
@Embeddable
public class SavedVacancyId implements Serializable {

    /**
     * Ідентифікатор кандидата, який зберіг вакансію
     */
    @Column(name = "who_saved")
    private Integer whoSaved;

    /**
     * Ідентифікатор вакансію, яку збережено
     */
    @Column(name = "vac_id")
    private Integer vacId;

    public SavedVacancyId() {

    }

    public SavedVacancyId(Integer whoSaved, Integer vacId) {
        this.whoSaved = whoSaved;
        this.vacId = vacId;
    }

    public Integer getWhoSaved() {
        return whoSaved;
    }

    public void setWhoSaved(Integer whoSaved) {
        this.whoSaved = whoSaved;
    }

    public Integer getVacId() {
        return vacId;
    }

    public void setVacId(Integer vacId) {
        this.vacId = vacId;
    }

    @Override
    public String toString() {
        return "SavedVacancyId{" +
                "whoSaved=" + whoSaved +
                ", vacId=" + vacId +
                '}';
    }
}
