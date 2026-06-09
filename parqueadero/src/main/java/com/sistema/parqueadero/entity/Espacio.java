package com.sistema.parqueadero.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "espacios")
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Long idEspacio;

    @Column(unique = true, nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false, length = 20)
    private String estado;
}