package com.example.empleados.infrastructure;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {

    Page<Empleado> findByActivoTrue(Pageable pageable);

    Optional<Empleado> findByIdPrefijoClaveAndIdNumeroClaveAndActivoTrue(String prefijoClave, Long numeroClave);
}
