package com.example.App.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                // Permitir acceso a login y recursos estáticos
                .requestMatchers("/login", "/resources/**").permitAll()
                // Restricción de acceso por rol
                .requestMatchers("/editar/**", "/agregar/**",
                        "/eliminar/**").hasRole("ADMIN")
                // Acceso a usuarios autenticados
                .requestMatchers("/").hasAnyRole("USER", "ADMIN")
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                .loginPage("/login") // Página de login personalizada
                .defaultSuccessUrl("/", true) // Redirigir después del login exitoso
                .permitAll() // Permitir acceso al formulario de login
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/errores/403")
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
