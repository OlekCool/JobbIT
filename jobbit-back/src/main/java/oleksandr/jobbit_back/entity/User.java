package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "The email can't be empty")
    @Column(unique = true)
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "The password can't be empty")
    private String userPassword;

    private LocalDate registrationDate;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    private LocalDateTime lastPasswordChanged;

    private Boolean verified;

    private String verificationToken;

    public User() {
    }

    public User(Integer id, String email, String userPassword, LocalDate registrationDate,
                Role userRole, LocalDateTime lastPasswordChanged, Boolean verified, String verificationToken) {
        this.id = id;
        this.email = email;
        this.userPassword = userPassword;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
        this.lastPasswordChanged = lastPasswordChanged;
        this.verified = verified;
        this.verificationToken = verificationToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getLastPasswordChanged() {
        return lastPasswordChanged;
    }

    public void setLastPasswordChanged(LocalDateTime lastPasswordChanged) {
        this.lastPasswordChanged = lastPasswordChanged;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
