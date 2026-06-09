package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {
}