package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Vehiculo;
import com.sistema.parqueadero.service.VehiculoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping
    public Vehiculo registrar(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.registrar(vehiculo);
    }

    @GetMapping
    public List<Vehiculo> listar() {
        return vehiculoService.listar();
    }
}