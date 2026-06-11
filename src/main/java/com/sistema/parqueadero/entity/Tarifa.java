package com.sistema.parqueadero.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    @Id
    private Long id = 1L; 
    
    @Column(nullable = false)
    private Double valorMinuto = 100.0; // Precio por defecto

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValorMinuto() { return valorMinuto; }
    public void setValorMinuto(Double valorMinuto) { this.valorMinuto = valorMinuto; }
}