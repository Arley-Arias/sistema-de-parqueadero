package com.sistema.parqueadero.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @Column(name = "placa", length = 10, nullable = false)
    private String placa;

    @Column(length = 50)
    private String marca;

    @ManyToOne
    @JoinColumn(name = "id_propietario", nullable = false)
    private Propietario propietario;
}