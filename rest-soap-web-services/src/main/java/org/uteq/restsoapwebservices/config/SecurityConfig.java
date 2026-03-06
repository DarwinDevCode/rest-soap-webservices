package org.uteq.restsoapwebservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF para facilitar pruebas en desarrollo (POST/PUT/DELETE)
                .csrf(csrf -> csrf.disable())
                // Configuración de CORS basada en el bean definido abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Reglas de autorización de rutas
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos y documentación
                        .requestMatchers("/api/catalogos/**", "/ws/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                        // Rutas de utilidad y autenticación permitidas para el flujo inicial
                        .requestMatchers("/api/usuarios/**", "/api/heroku/**").permitAll()
                        .requestMatchers("/login", "/logout", "/oauth2/**", "/error").permitAll()
                        // Cualquier otra petición requiere una sesión activa
                        .anyRequest().authenticated()
                )
                // Configuración del login OAuth2
                .oauth2Login(oauth2 -> oauth2
                        // Redirección automática al perfil tras un login exitoso
                        .defaultSuccessUrl("/api/usuarios/perfil", true)
                )
                // Configuración del cierre de sesión
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}