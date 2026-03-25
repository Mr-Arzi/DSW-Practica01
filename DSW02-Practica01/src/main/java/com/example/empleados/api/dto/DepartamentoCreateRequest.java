package com.example.empleados.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartamentoCreateRequest(
        @NotBlank @Size(max = 50) String clave,
        @NotBlank @Size(max = 100) String nombre
) {
}
