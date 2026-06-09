package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
}