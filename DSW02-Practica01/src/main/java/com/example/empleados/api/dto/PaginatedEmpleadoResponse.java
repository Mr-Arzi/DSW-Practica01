package com.example.empleados.api.dto;

import java.util.List;

public record PaginatedEmpleadoResponse(
        int page,
        int limit,
        long total,
        List<EmpleadoResponse> data
) {
}
