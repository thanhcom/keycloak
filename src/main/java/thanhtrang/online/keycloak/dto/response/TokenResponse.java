package thanhtrang.online.keycloak.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenResponse {
    @NotBlank(message = "New token must not be blank")
    private String newToken;
}
