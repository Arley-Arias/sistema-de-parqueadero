package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByFechaSalidaIsNull();
}