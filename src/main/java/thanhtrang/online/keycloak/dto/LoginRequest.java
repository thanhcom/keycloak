package thanhtrang.online.keycloak.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username field is required !!!")
    @Size(min = 6, message = "username must be at least 6 character !!!")
    private String username;
    @NotBlank(message = "pass field is required !!!")
    @Size(min = 6, message = "pass must be at least 6 character !!!")
    private String password;
}