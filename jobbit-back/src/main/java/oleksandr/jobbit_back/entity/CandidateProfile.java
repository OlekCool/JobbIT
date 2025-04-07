package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cand_profile ")
@Data
public class CandidateProfile {

    @Id
    @Column(name = "cand_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "some_data")
    private String someData;

    @Override
    public String toString() {
        return "CandidateProfile{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "no user") +
                '}';
    }
}
