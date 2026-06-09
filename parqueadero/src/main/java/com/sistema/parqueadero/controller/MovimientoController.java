package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Movimiento;
import com.sistema.parqueadero.service.MovimientoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping("/ingreso")
    public Movimiento registrarIngreso(@RequestParam String placa, @RequestParam Long idUsuario) {
        return movimientoService.registrarIngreso(placa, idUsuario);
    }

    @PutMapping("/salida/{idMovimiento}")
    public Movimiento registrarSalida(@PathVariable Long idMovimiento) {
        return movimientoService.registrarSalida(idMovimiento);
    }
}