package com.example.empleados.application;

import com.example.empleados.api.dto.DepartamentoCreateRequest;
import com.example.empleados.api.dto.DepartamentoResponse;
import com.example.empleados.api.dto.DepartamentoUpdateRequest;
import com.example.empleados.application.exception.BadRequestException;
import com.example.empleados.application.exception.ConflictException;
import com.example.empleados.application.exception.ResourceNotFoundException;
import com.example.empleados.domain.Departamento;
import com.example.empleados.infrastructure.DepartamentoRepository;
import com.example.empleados.infrastructure.EmpleadoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoService(
            DepartamentoRepository departamentoRepository,
            EmpleadoRepository empleadoRepository
    ) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public DepartamentoResponse create(DepartamentoCreateRequest request) {
        String clave = normalizeClave(request.clave());
        String nombre = normalizeNombre(request.nombre());

        if (departamentoRepository.existsByClave(clave)) {
            throw new ConflictException("Ya existe un departamento con la clave proporcionada");
        }

        Departamento departamento = new Departamento(clave, nombre);
        return DepartamentoResponse.from(departamentoRepository.save(departamento));
    }

    @Transactional(readOnly = true)
    public List<DepartamentoResponse> listAll() {
        return departamentoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(DepartamentoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartamentoResponse findById(Long id) {
        return DepartamentoResponse.from(getDepartamento(id));
    }

    @Transactional
    public DepartamentoResponse update(Long id, DepartamentoUpdateRequest request) {
        Departamento departamento = getDepartamento(id);
        String clave = normalizeClave(request.clave());
        String nombre = normalizeNombre(request.nombre());

        if (departamentoRepository.existsByClaveAndIdNot(clave, id)) {
            throw new ConflictException("Ya existe un departamento con la clave proporcionada");
        }

        departamento.updateDatos(clave, nombre);
        return DepartamentoResponse.from(departamentoRepository.save(departamento));
    }

    @Transactional
    public void deleteById(Long id) {
        Departamento departamento = getDepartamento(id);
        long asociados = empleadoRepository.countByDepartamento_Id(id);
        if (asociados > 0) {
            throw new ConflictException("No se puede eliminar el departamento porque tiene empleados asociados");
        }
        departamentoRepository.delete(Objects.requireNonNull(departamento));
    }

    private Departamento getDepartamento(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("Departamento no encontrado");
        }
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento no encontrado"));
    }

    private String normalizeClave(String clave) {
        if (clave == null) {
            throw new BadRequestException("La clave del departamento es obligatoria");
        }
        String trimmed = clave.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestException("La clave del departamento es obligatoria");
        }
        return trimmed.toUpperCase(Locale.ROOT);
    }

    private String normalizeNombre(String nombre) {
        if (nombre == null) {
            throw new BadRequestException("El nombre del departamento es obligatorio");
        }
        String trimmed = nombre.trim();
        if (trimmed.isEmpty()) {
            throw new BadRequestException("El nombre del departamento es obligatorio");
        }
        return trimmed;
    }
}
