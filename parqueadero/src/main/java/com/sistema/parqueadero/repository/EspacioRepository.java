package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {
}