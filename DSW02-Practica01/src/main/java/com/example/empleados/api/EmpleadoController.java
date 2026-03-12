package com.example.empleados.api;

import com.example.empleados.api.dto.EmpleadoCreateRequest;
import com.example.empleados.api.dto.PaginatedEmpleadoResponse;
import com.example.empleados.api.dto.EmpleadoResponse;
import com.example.empleados.api.dto.EmpleadoUpdateRequest;
import com.example.empleados.application.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/empleados")
@Validated
@Tag(name = "Empleados", description = "API para la gestión de empleados")
@SecurityRequirement(name = "basicAuth")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    @ApiResponse(responseCode = "201", description = "Empleado creado")
    public ResponseEntity<EmpleadoResponse> create(@Valid @RequestBody EmpleadoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar empleados activos")
    @ApiResponse(responseCode = "200", description = "Lista de empleados activos")
    public ResponseEntity<PaginatedEmpleadoResponse> listActivos(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit
    ) {
        return ResponseEntity.ok(empleadoService.listActivos(page, limit));
    }

    @GetMapping("/{clave}")
    @Operation(summary = "Consultar empleado activo por clave")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado")
    public ResponseEntity<EmpleadoResponse> getByClave(@PathVariable String clave) {
        return ResponseEntity.ok(empleadoService.getByClave(clave));
    }

    @PutMapping("/{clave}")
    @Operation(summary = "Actualizar empleado activo")
    @ApiResponse(responseCode = "200", description = "Empleado actualizado")
    public ResponseEntity<EmpleadoResponse> update(
            @PathVariable String clave,
            @Valid @RequestBody EmpleadoUpdateRequest request
    ) {
        return ResponseEntity.ok(empleadoService.updateByClave(clave, request));
    }

    @DeleteMapping("/{clave}")
    @Operation(summary = "Baja lógica de empleado")
    @ApiResponse(responseCode = "204", description = "Empleado marcado como inactivo")
    public ResponseEntity<Void> delete(@PathVariable String clave) {
        empleadoService.deleteByClave(clave);
        return ResponseEntity.noContent().build();
    }
}
