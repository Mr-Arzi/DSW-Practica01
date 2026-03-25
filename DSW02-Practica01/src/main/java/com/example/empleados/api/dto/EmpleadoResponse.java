package com.example.empleados.api.dto;

import com.example.empleados.domain.Empleado;

public record EmpleadoResponse(
        String prefijoClave,
        Long numeroClave,
        String clave,
        String nombre,
        String direccion,
        String telefono,
        Boolean activo,
        String email,
        Long departamentoId,
        String departamentoClave,
        String departamentoNombre
) {
    public static EmpleadoResponse from(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getPrefijoClave(),
                empleado.getNumeroClave(),
                empleado.getClave(),
                empleado.getNombre(),
                empleado.getDireccion(),
                empleado.getTelefono(),
                empleado.getActivo(),
                empleado.getEmail(),
                empleado.getDepartamentoId(),
                empleado.getDepartamentoClave(),
                empleado.getDepartamentoNombre()
        );
    }
}
