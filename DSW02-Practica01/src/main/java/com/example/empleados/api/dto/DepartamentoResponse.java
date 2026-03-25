package com.example.empleados.api.dto;

import com.example.empleados.domain.Departamento;

public record DepartamentoResponse(
        Long id,
        String clave,
        String nombre
) {
    public static DepartamentoResponse from(Departamento departamento) {
        return new DepartamentoResponse(
                departamento.getId(),
                departamento.getClave(),
                departamento.getNombre()
        );
    }
}
