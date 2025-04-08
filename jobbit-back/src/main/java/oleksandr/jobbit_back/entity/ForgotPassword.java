package oleksandr.jobbit_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;

import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
@Data
public class ForgotPassword {

    @Id
    @Column(name = "fpid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    @Column(name = "otp", nullable = false)
    private Integer otp;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
