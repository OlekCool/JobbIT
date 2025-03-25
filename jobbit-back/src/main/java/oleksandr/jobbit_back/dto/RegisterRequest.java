package oleksandr.jobbit_back.dto;

import lombok.*;
import oleksandr.jobbit_back.entity.Role;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
