package thanhtrang.online.keycloak.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Bắt buộc để @PreAuthorize hoạt động
public class SecurityConfig {

    // -------------------- CHAIN CHO LOGIN PUBLIC --------------------
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/admin/user/login") // Chỉ áp dụng cho endpoint login
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Cho phép public
                .csrf(csrf -> csrf.disable()); // Tắt CSRF cho login endpoint

        return http.build();
    }

    // -------------------- CHAIN CHO CÁC ENDPOINT KHÁC --------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public").permitAll() // Public endpoint
                        .anyRequest().authenticated()           // Các endpoint khác cần JWT
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

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
