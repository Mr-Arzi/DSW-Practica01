package com.example.empleados.security;

import com.example.empleados.domain.Empleado;
import com.example.empleados.infrastructure.EmpleadoRepository;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Locale;

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
            .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/empleados").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/empleados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/empleados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/departamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/departamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/departamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/departamentos", "/api/v1/departamentos/**").hasAnyRole("ADMIN", "LECTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/empleados", "/api/v1/empleados/**").hasAnyRole("ADMIN", "LECTOR")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder, EmpleadoRepository empleadoRepository) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        UserDetails adminEmailAlias = User.builder()
            .username(adminUsername + "@example.com")
            .password(passwordEncoder.encode(adminPassword))
            .roles("ADMIN")
            .build();

        UserDetails lector = User.builder()
                .username(lectorUsername)
                .password(passwordEncoder.encode(lectorPassword))
                .roles("LECTOR")
                .build();

        UserDetails lectorEmailAlias = User.builder()
            .username(lectorUsername + "@example.com")
            .password(passwordEncoder.encode(lectorPassword))
            .roles("LECTOR")
            .build();

        InMemoryUserDetailsManager inMemoryUsers = new InMemoryUserDetailsManager(
                admin,
                adminEmailAlias,
                lector,
                lectorEmailAlias
        );

        return username -> {
            try {
                return inMemoryUsers.loadUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
                String normalizedEmail = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
                Empleado empleado = empleadoRepository.findByEmailAndActivoTrue(normalizedEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                if (empleado.getPasswordHash() == null || empleado.getPasswordHash().isBlank()) {
                    throw new UsernameNotFoundException("Usuario sin credenciales activas");
                }

                return User.builder()
                        .username(normalizedEmail)
                        .password(empleado.getPasswordHash())
                        .roles("LECTOR")
                        .build();
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        return source;
    }
}
