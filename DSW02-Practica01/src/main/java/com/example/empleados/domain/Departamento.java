package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clave", nullable = false, length = 50, unique = true)
    private String clave;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Departamento() {
    }

    public Departamento(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void updateDatos(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }
}
