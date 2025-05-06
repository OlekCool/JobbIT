package oleksandr.jobbit_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

/**
 * Клас для збереження складеного первинного ключа в БД
 */
@Embeddable
@Data
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

    /**
     * Пустий конструктор
     */
    public SavedVacancyId() {

    }

    /**
     * Конструктор з даними ідентифікаторів
     * @param whoSaved id, хто зберіг
     * @param vacId id вакансії
     */
    public SavedVacancyId(Integer whoSaved, Integer vacId) {
        this.whoSaved = whoSaved;
        this.vacId = vacId;
    }
}
