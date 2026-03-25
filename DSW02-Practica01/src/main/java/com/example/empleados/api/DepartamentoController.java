package com.example.empleados.api;

import com.example.empleados.api.dto.DepartamentoCreateRequest;
import com.example.empleados.api.dto.DepartamentoResponse;
import com.example.empleados.api.dto.DepartamentoUpdateRequest;
import com.example.empleados.application.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departamentos")
@Tag(name = "Departamentos", description = "API para la gestión de departamentos")
@SecurityRequirement(name = "basicAuth")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    @Operation(summary = "Crear departamento")
    @ApiResponse(responseCode = "201", description = "Departamento creado")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "409", description = "Duplicidad de clave")
    @ApiResponse(responseCode = "401", description = "Missing or invalid credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden for role")
    public ResponseEntity<DepartamentoResponse> create(@Valid @RequestBody DepartamentoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar departamentos")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos")
    @ApiResponse(responseCode = "401", description = "Missing or invalid credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden for role")
    public ResponseEntity<List<DepartamentoResponse>> listAll() {
        return ResponseEntity.ok(departamentoService.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar departamento por id")
    @ApiResponse(responseCode = "200", description = "Departamento encontrado")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @ApiResponse(responseCode = "401", description = "Missing or invalid credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden for role")
    public ResponseEntity<DepartamentoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar departamento")
    @ApiResponse(responseCode = "200", description = "Departamento actualizado")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @ApiResponse(responseCode = "409", description = "Duplicidad de clave")
    @ApiResponse(responseCode = "401", description = "Missing or invalid credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden for role")
    public ResponseEntity<DepartamentoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoUpdateRequest request
    ) {
        return ResponseEntity.ok(departamentoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar departamento")
    @ApiResponse(responseCode = "204", description = "Departamento eliminado")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad")
    @ApiResponse(responseCode = "401", description = "Missing or invalid credentials")
    @ApiResponse(responseCode = "403", description = "Forbidden for role")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
