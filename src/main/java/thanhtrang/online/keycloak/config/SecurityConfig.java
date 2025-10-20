package thanhtrang.online.keycloak.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Bắt buộc để @PreAuthorize hoạt động
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        String[] publicEndpoints = {
                "/api/admin/user/login",
                "/api/admin/user/refresh-token"
        };

        http
                .securityMatcher(publicEndpoints) // Áp dụng rule cho các endpoint này
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable); // Tắt CSRF cho nhóm endpoint public

        return http.build();
    }


    // -------------------- JWT DECODER --------------------
    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        return JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri());
    }

    // -------------------- ROLE CONVERTER --------------------
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
