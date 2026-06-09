package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {
    List<Espacio> findByEstado(String estado);
}