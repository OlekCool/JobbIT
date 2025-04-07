package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true)
    @NotBlank(message = "The email can't be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @Column(name = "user_password")
    @NotBlank(message = "The password can't be empty")
    private String userPassword;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(name = "last_password_changed")
    private LocalDateTime lastPasswordChanged;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verification_token")
    private String verificationToken;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
    private ForgotPassword forgotPassword;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CandidateProfile candidateProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RecruiterProfile recruiterProfile;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", userRole=" + userRole +
                ", verified=" + verified +
                '}';
    }
}
