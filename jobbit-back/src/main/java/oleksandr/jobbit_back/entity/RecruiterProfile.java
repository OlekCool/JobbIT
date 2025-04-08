package oleksandr.jobbit_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Table(name = "recr_profile ")
@Data
public class RecruiterProfile {

    @Id
    @Column(name = "recr_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "some_data")
    private String someData;

    @Override
    public String toString() {
        return "RecruiterProfile{"
                + "id=" + id
                + ", user=" + (user != null ? user.getEmail() : "no user")
                + '}';
    }
}
