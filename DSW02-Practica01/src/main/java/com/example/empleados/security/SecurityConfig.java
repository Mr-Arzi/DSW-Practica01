package com.example.empleados.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final String adminUsername;
    private final String adminPassword;
    private final String lectorUsername;
    private final String lectorPassword;

    public SecurityConfig(
            @Value("${app.security.admin.username}") String adminUsername,
            @Value("${app.security.admin.password}") String adminPassword,
            @Value("${app.security.lector.username:lector}") String lectorUsername,
            @Value("${app.security.lector.password:lector123}") String lectorPassword
    ) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.lectorUsername = lectorUsername;
        this.lectorPassword = lectorPassword;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/empleados").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/empleados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/empleados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/empleados", "/api/v1/empleados/**").hasAnyRole("ADMIN", "LECTOR")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        UserDetails lector = User.builder()
                .username(lectorUsername)
                .password(passwordEncoder.encode(lectorPassword))
                .roles("LECTOR")
                .build();

        return new InMemoryUserDetailsManager(admin, lector);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
