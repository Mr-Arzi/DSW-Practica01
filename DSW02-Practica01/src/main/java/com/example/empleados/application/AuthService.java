package com.example.empleados.application;

import com.example.empleados.api.dto.LoginRequest;
import com.example.empleados.api.dto.LoginSuccessResponse;
import com.example.empleados.application.exception.InvalidCredentialsException;
import com.example.empleados.domain.Empleado;
import com.example.empleados.infrastructure.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginSuccessResponse authenticate(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        Empleado empleado = empleadoRepository.findByEmailAndActivoTrue(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE));

        if (empleado.getPasswordHash() == null || !passwordEncoder.matches(request.password(), empleado.getPasswordHash())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        return new LoginSuccessResponse("Authentication successful");
    }
}
