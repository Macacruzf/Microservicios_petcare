package com.petcare.producto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.http.HttpMethod;

import org.springframework.security.core.userdetails.User;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para facilitar pruebas
            .cors(Customizer.withDefaults()) // CORS abierto para frontend
            .anonymous(Customizer.withDefaults()) // Permite acceso sin login
            .authorizeHttpRequests(auth -> auth
                // Swagger público
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Rutas GET públicas
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                
                // Rutas protegidas para ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                
                // Cualquier otra ruta, permitir sin seguridad extra
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()); // Permite autenticación básica (admin/admin)

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        // Usuario en memoria: admin / admin
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password("{noop}admin") // NoOp: sin encriptar (solo para pruebas locales)
                .roles("ADMIN")
                .build()
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Permitir todos los orígenes
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}