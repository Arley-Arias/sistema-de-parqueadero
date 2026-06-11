package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    @Query(value = "SELECT COALESCE(SUM(valor_pagado), 0) FROM movimientos WHERE DATE(fecha_salida) = CURDATE()", nativeQuery = true)
Double sumarRecaudoHoy();
}