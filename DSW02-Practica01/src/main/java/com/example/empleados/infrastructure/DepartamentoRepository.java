package com.example.empleados.infrastructure;

import com.example.empleados.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByClave(String clave);

    boolean existsByClave(String clave);

    boolean existsByClaveAndIdNot(String clave, Long id);
}
