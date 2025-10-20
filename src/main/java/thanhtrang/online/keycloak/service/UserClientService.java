package thanhtrang.online.keycloak.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import thanhtrang.online.keycloak.dto.response.LoginResponse;
import thanhtrang.online.keycloak.enums.ErrCode;
import thanhtrang.online.keycloak.exception.AppException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserClientService {
    private final Keycloak keycloak;
    private final String realm = "thanhcom";
    private final String clientId = "springboot"; // thay bằng client của bạn
    private final String serverUrl = "http://192.168.1.66:8087"; // URL Keycloak


    public void createUser(String username, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        keycloak.realm(realm)
                .users()
                .create(user);
    }

    public void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    public String getUserIdByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .search(username);

        return users.isEmpty() ? null : users.get(0).getId();
    }

    public void updateUser(String userId, String newEmail) {
        UserResource user = keycloak.realm(realm)
                .users()
                .get(userId);

        UserRepresentation userRep = user.toRepresentation();
        userRep.setEmail(newEmail);

        user.update(userRep);
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm)
                .users()
                .get(userId)
                .remove();
    }

    // =================== ROLE METHODS ===================

    /**
     * Lấy danh sách role của người dùng
     */
    public List<String> getRoles(String userId) {
        UserResource user = keycloak.realm(realm)
                .users()
                .get(userId);

        List<RoleRepresentation> roles = user.roles()
                .realmLevel()
                .listAll(); // list realm-level roles

        return roles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    /**
     * Gán role cho người dùng
     */
    public void assignRole(String userId, String roleName) {
        UserResource user = keycloak.realm(realm)
                .users()
                .get(userId);

        RoleRepresentation role = keycloak.realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        user.roles()
                .realmLevel()
                .add(List.of(role));
    }

    public LoginResponse login(String username, String password) {
        try {
            Keycloak keycloakUser = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .build();

            AccessTokenResponse tokenResponse = keycloakUser.tokenManager().getAccessToken();

            return LoginResponse.builder()
                    .access_token(tokenResponse.getToken())
                    .refresh_token(tokenResponse.getRefreshToken())
                    .expires_in(tokenResponse.getExpiresIn())
                    .refresh_expires_in(tokenResponse.getRefreshExpiresIn())
                    .token_type(tokenResponse.getTokenType())
                    .scope(tokenResponse.getScope())
                    .success(true)
                    .message("Login successful")
                    .build();

        } catch (Exception e) {
            throw  new AppException(ErrCode.UNAUTHORIZED);
        }
    }

    public LoginResponse refreshToken(String refreshToken) {
        try {
            String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            Client client = ClientBuilder.newClient();

            Form form = new Form()
                    .param("grant_type", "refresh_token")
                    .param("client_id", clientId)
                    .param("refresh_token", refreshToken);

            Response response = client.target(tokenUrl)
                    .request()
                    .post(Entity.form(form));

            if (response.getStatus() != 200) {
                throw new AppException(ErrCode.UNAUTHORIZED);
            }

            AccessTokenResponse tokenResponse = response.readEntity(AccessTokenResponse.class);

            return LoginResponse.builder()
                    .access_token(tokenResponse.getToken())
                    .refresh_token(tokenResponse.getRefreshToken())
                    .expires_in(tokenResponse.getExpiresIn())
                    .refresh_expires_in(tokenResponse.getRefreshExpiresIn())
                    .token_type(tokenResponse.getTokenType())
                    .scope(tokenResponse.getScope())
                    .success(true)
                    .message("Token refreshed successfully")
                    .build();

        } catch (Exception e) {
            throw new AppException(ErrCode.UNAUTHORIZED);
        }
    }


}
