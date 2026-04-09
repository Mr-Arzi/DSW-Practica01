package com.example.empleados.application;

import com.example.empleados.api.dto.LoginRequest;
import com.example.empleados.api.dto.LoginSuccessResponse;
import com.example.empleados.application.exception.InvalidCredentialsException;
import com.example.empleados.domain.Empleado;
import com.example.empleados.infrastructure.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;
    private final String lectorUsername;
    private final String lectorPassword;

    public AuthService(
            EmpleadoRepository empleadoRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.admin.username}") String adminUsername,
            @Value("${app.security.admin.password}") String adminPassword,
            @Value("${app.security.lector.username:lector}") String lectorUsername,
            @Value("${app.security.lector.password:lector123}") String lectorPassword
    ) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.lectorUsername = lectorUsername;
        this.lectorPassword = lectorPassword;
    }

    @Transactional(readOnly = true)
    public LoginSuccessResponse authenticate(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (isPredefinedUser(normalizedEmail, request.password())) {
            return new LoginSuccessResponse("Authentication successful");
        }

        Empleado empleado = empleadoRepository.findByEmailAndActivoTrue(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE));

        if (empleado.getPasswordHash() == null || !passwordEncoder.matches(request.password(), empleado.getPasswordHash())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        return new LoginSuccessResponse("Authentication successful");
    }

    private boolean isPredefinedUser(String usernameOrEmail, String rawPassword) {
        String normalizedAdmin = adminUsername.trim().toLowerCase(Locale.ROOT);
        String normalizedLector = lectorUsername.trim().toLowerCase(Locale.ROOT);

        boolean isAdmin = usernameOrEmail.equals(normalizedAdmin)
                || usernameOrEmail.equals(normalizedAdmin + "@example.com");
        if (isAdmin) {
            return adminPassword.equals(rawPassword);
        }

        boolean isLector = usernameOrEmail.equals(normalizedLector)
                || usernameOrEmail.equals(normalizedLector + "@example.com");
        if (isLector) {
            return lectorPassword.equals(rawPassword);
        }

        return false;
    }
}
