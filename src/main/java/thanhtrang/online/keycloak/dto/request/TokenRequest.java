package thanhtrang.online.keycloak.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRequest {
    @NotBlank(message = "Refresh token token must not be blank")
    private String refresh_token;
}
