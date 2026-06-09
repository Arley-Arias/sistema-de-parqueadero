package com.sistema.parqueadero.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/panel/vehiculos/eliminar/**").hasRole("ADMIN") // Solo el ADMIN puede borrar
                .requestMatchers("/panel/**").hasAnyRole("ADMIN", "OPERADOR") // Ambos pueden ver el panel
                .anyRequest().permitAll()
            )
            .formLogin(login -> login
                .defaultSuccessUrl("/panel/vehiculos", true) // A dónde los manda al iniciar sesión
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin123")
            .roles("ADMIN")
            .build();

        UserDetails operador = User.withDefaultPasswordEncoder()
            .username("operador")
            .password("operador123")
            .roles("OPERADOR")
            .build();

        return new InMemoryUserDetailsManager(admin, operador);
    }
}