package com.example.empleados.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EmpleadoUpdateRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String direccion,
        @NotBlank @Size(max = 100) @Pattern(regexp = "^[0-9]{1,100}$") String telefono,
        @NotNull @Positive Long departamentoId
) {
}
