package thanhtrang.online.keycloak.service;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserClientService {
    Keycloak keycloak;
    public void createUser(String username, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        keycloak.realm("thanhcom")
                .users()
                .create(user);
    }
    public void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm("thanhcom")
                .users()
                .get(userId)
                .resetPassword(credential);
    }
    public String getUserIdByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm("thanhcom")
                .users()
                .search(username);

        return users.isEmpty() ? null : users.get(0).getId();
    }
    public void updateUser(String userId, String newEmail) {
        UserResource user = keycloak.realm("thanhcom")
                .users()
                .get(userId);

        UserRepresentation userRep = user.toRepresentation();
        userRep.setEmail(newEmail);

        user.update(userRep);
    }
    public void deleteUser(String userId) {
        keycloak.realm("thanhcom")
                .users()
                .get(userId)
                .remove();
    }
}
