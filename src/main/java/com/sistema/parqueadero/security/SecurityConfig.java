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
        .requestMatchers("/", "/login", "/css/**").permitAll()
        .requestMatchers("/panel/configuracion", "/panel/propietarios/**", "/panel/vehiculos/eliminar/**").hasRole("ADMIN")
        .requestMatchers("/panel/**").hasAnyRole("ADMIN", "OPERADOR")
        .anyRequest().authenticated()
        )
            .formLogin(login -> login
                .loginPage("/login") 
                .defaultSuccessUrl("/panel/vehiculos", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll()); // Vuelve al inicio al salir

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