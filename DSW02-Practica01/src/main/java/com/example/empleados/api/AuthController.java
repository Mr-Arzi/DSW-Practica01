package com.example.empleados.api;

import com.example.empleados.api.dto.LoginErrorResponse;
import com.example.empleados.api.dto.LoginRequest;
import com.example.empleados.api.dto.LoginSuccessResponse;
import com.example.empleados.application.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Tag(name = "Auth", description = "API de autenticación de empleados")
@SecurityRequirement(name = "")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar credenciales de empleado",
            security = {@SecurityRequirement(name = "")}
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful")
    @ApiResponse(
            responseCode = "400",
            description = "Request validation error",
            content = @Content(schema = @Schema(implementation = LoginErrorResponse.class))
    )
        @ApiResponse(
            responseCode = "401",
            description = "Invalid email or password",
            content = @Content(schema = @Schema(implementation = LoginErrorResponse.class))
        )
    public ResponseEntity<LoginSuccessResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
