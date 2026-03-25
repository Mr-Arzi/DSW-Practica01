package com.example.empleados.application;

import com.example.empleados.api.dto.EmpleadoCreateRequest;
import com.example.empleados.api.dto.PaginatedEmpleadoResponse;
import com.example.empleados.api.dto.EmpleadoResponse;
import com.example.empleados.api.dto.EmpleadoUpdateRequest;
import com.example.empleados.application.exception.ConflictException;
import com.example.empleados.application.exception.InvalidClaveException;
import com.example.empleados.application.exception.ResourceNotFoundException;
import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.infrastructure.DepartamentoRepository;
import com.example.empleados.infrastructure.EmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmpleadoService {

    private static final String CLAVE_PREFIX = "EMP-";
    private static final Pattern CLAVE_PATTERN = Pattern.compile("^EMP-(\\d+)$");

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(
            EmpleadoRepository empleadoRepository,
            DepartamentoRepository departamentoRepository,
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder
    ) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmpleadoResponse create(EmpleadoCreateRequest request) {
        Long nextNumero = jdbcTemplate.queryForObject("SELECT nextval('empleado_numero_clave_seq')", Long.class);
        String normalizedEmail = normalizeEmail(request.email());
        String passwordHash = hashPassword(request.password());
        Departamento departamento = resolveDepartamento(request.departamentoId());

        Empleado empleado = new Empleado(
                new EmpleadoId(CLAVE_PREFIX, nextNumero),
                request.nombre().trim(),
                request.direccion().trim(),
                request.telefono().trim(),
                true,
                normalizedEmail,
                passwordHash,
                departamento
        );
        try {
            return EmpleadoResponse.from(empleadoRepository.saveAndFlush(empleado));
        } catch (DataIntegrityViolationException exception) {
            String causeMessage = exception.getMostSpecificCause() != null
                    ? exception.getMostSpecificCause().getMessage()
                    : "";

            if (causeMessage != null && causeMessage.contains("ux_empleados_email")) {
                throw new ConflictException("El correo ya esta registrado");
            }

            throw new ConflictException("No fue posible crear el empleado con los datos proporcionados");
        }
    }

    @Transactional(readOnly = true)
    public PaginatedEmpleadoResponse listActivos(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id.numeroClave").ascending());
        Page<Empleado> empleadosPage = empleadoRepository.findByActivoTrue(pageable);

        if (page > empleadosPage.getTotalPages() && empleadosPage.getTotalElements() > 0) {
            return new PaginatedEmpleadoResponse(page, limit, empleadosPage.getTotalElements(), Collections.emptyList());
        }

        List<EmpleadoResponse> data = empleadosPage.getContent().stream().map(EmpleadoResponse::from).toList();
        return new PaginatedEmpleadoResponse(page, limit, empleadosPage.getTotalElements(), data);
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse getByClave(String clave) {
        EmpleadoId empleadoId = parseClave(clave);
        return empleadoRepository
                .findByIdPrefijoClaveAndIdNumeroClaveAndActivoTrue(empleadoId.getPrefijoClave(), empleadoId.getNumeroClave())
                .map(EmpleadoResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado en catálogo activo"));
    }

    @Transactional
    public EmpleadoResponse updateByClave(String clave, EmpleadoUpdateRequest request) {
        EmpleadoId empleadoId = parseClave(clave);
        Empleado empleado = empleadoRepository.findById(Objects.requireNonNull(empleadoId))
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        if (!Boolean.TRUE.equals(empleado.getActivo())) {
            throw new ResourceNotFoundException("Empleado no disponible en catálogo activo");
        }

        empleado.updateDatos(request.nombre().trim(), request.direccion().trim(), request.telefono().trim());
        empleado.assignDepartamento(resolveDepartamento(request.departamentoId()));
        return EmpleadoResponse.from(empleadoRepository.save(empleado));
    }

    @Transactional
    public void deleteByClave(String clave) {
        EmpleadoId empleadoId = parseClave(clave);
        Empleado empleado = empleadoRepository.findById(Objects.requireNonNull(empleadoId))
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        if (!Boolean.TRUE.equals(empleado.getActivo())) {
            throw new ConflictException("Operación no aplicable: empleado ya inactivo");
        }

        empleado.desactivar();
        empleadoRepository.save(empleado);
    }

    private EmpleadoId parseClave(String clave) {
        Matcher matcher = CLAVE_PATTERN.matcher(clave);
        if (!matcher.matches()) {
            throw new InvalidClaveException("Formato de clave inválido. Se espera EMP-<autonumérico>");
        }
        return new EmpleadoId(CLAVE_PREFIX, Long.parseLong(matcher.group(1)));
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    private String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            return null;
        }
        return passwordEncoder.encode(password);
    }

    private Departamento resolveDepartamento(Long departamentoId) {
        if (departamentoId == null) {
            throw new ResourceNotFoundException("Departamento no encontrado");
        }
        return departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento no encontrado"));
    }
}
