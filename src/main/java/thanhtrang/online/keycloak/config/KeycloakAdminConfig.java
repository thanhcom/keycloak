package thanhtrang.online.keycloak.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://192.168.1.66:8087")
                .realm("master") // quản trị thì dùng realm master
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();
    }
}

