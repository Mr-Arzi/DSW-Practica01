package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmpleadoId implements Serializable {

    @Column(name = "prefijo_clave", nullable = false, length = 4)
    private String prefijoClave;

    @Column(name = "numero_clave", nullable = false)
    private Long numeroClave;

    public EmpleadoId() {
    }

    public EmpleadoId(String prefijoClave, Long numeroClave) {
        this.prefijoClave = prefijoClave;
        this.numeroClave = numeroClave;
    }

    public String getPrefijoClave() {
        return prefijoClave;
    }

    public Long getNumeroClave() {
        return numeroClave;
    }

    public String toClave() {
        return prefijoClave + numeroClave;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EmpleadoId empleadoId)) {
            return false;
        }
        return Objects.equals(prefijoClave, empleadoId.prefijoClave)
                && Objects.equals(numeroClave, empleadoId.numeroClave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefijoClave, numeroClave);
    }
}
