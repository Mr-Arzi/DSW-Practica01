package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "empleados")
public class Empleado {

    @EmbeddedId
    private EmpleadoId id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 100)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false, foreignKey = @ForeignKey(name = "fk_empleados_departamento"))
    private Departamento departamento;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Empleado() {
    }

    public Empleado(EmpleadoId id, String nombre, String direccion, String telefono, Boolean activo) {
        this(id, nombre, direccion, telefono, activo, null, null, null);
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            Boolean activo,
            String email,
            String passwordHash,
            Departamento departamento
    ) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
        this.email = email;
        this.passwordHash = passwordHash;
        this.departamento = departamento;
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            Boolean activo,
            String email,
            String passwordHash
    ) {
        this(id, nombre, direccion, telefono, activo, email, passwordHash, null);
    }

    public EmpleadoId getId() {
        return id;
    }

    public String getPrefijoClave() {
        return id.getPrefijoClave();
    }

    public Long getNumeroClave() {
        return id.getNumeroClave();
    }

    public String getClave() {
        return id.toClave();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Long getDepartamentoId() {
        return departamento != null ? departamento.getId() : null;
    }

    public String getDepartamentoClave() {
        return departamento != null ? departamento.getClave() : null;
    }

    public String getDepartamentoNombre() {
        return departamento != null ? departamento.getNombre() : null;
    }

    public void updateDatos(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public void assignDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void updateCredenciales(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
