package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Movimiento;
import com.sistema.parqueadero.repository.MovimientoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final MovimientoRepository movimientoRepository;

    public ReporteController(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @GetMapping("/activos")
    public List<Movimiento> listarActivos() {
        return movimientoRepository.findByFechaSalidaIsNull();
    }
}