package oleksandr.jobbit_back.entity;

import jakarta.persistence.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

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

    public User() {
    }

    public User(Integer id, String email, String userPassword, LocalDate registrationDate, Role userRole) {
        this.id = id;
        this.email = email;
        this.userPassword = userPassword;
        this.registrationDate = registrationDate;
        this.userRole = userRole;
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
}
