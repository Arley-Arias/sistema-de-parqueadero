package com.sistema.parqueadero.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "espacios")
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero; // Ejemplo: A1, B2, C3

    @Column(nullable = false)
    private String estado = "DISPONIBLE"; // DISPONIBLE u OCUPADO

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}