package oleksandr.jobbit_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клас для збереження складеного первинного ключа в БД
 */
@Embeddable
@Data
@Getter
@Setter
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

    /**
     * Пустий конструктор
     */
    public AppliedVacancyId() {

    }

    /**
     * Конструктор з даними ідентифікаторів
     * @param whoApplied id, хто відгукнувся
     * @param vacId id вакансії
     */
    public AppliedVacancyId(Integer whoApplied, Integer vacId) {
        this.whoApplied = whoApplied;
        this.vacId = vacId;
    }

}
