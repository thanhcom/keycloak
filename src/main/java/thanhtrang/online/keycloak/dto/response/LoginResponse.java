package thanhtrang.online.keycloak.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String access_token;
    private String refresh_token;
    private long expires_in;
    private long refresh_expires_in;
    private String token_type;
    private String scope;
    private boolean success;
    private String message;
}