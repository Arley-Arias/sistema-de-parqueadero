package com.sistema.parqueadero.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll() // Dejamos crear usuarios libremente
                .requestMatchers("/api/v1/movimientos/**").hasAnyRole("ADMIN", "OPERADOR") // Ambos pueden registrar carros
                .requestMatchers("/api/v1/reportes/**").hasRole("ADMIN") // Solo el admin ve reportes
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // Usamos autenticación básica para Thunder Client
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usamos este sin encriptar solo para efectos de la prueba rápida.
        return NoOpPasswordEncoder.getInstance(); 
    }
}