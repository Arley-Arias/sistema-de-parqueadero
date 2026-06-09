package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Espacio;
import com.sistema.parqueadero.service.EspacioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/espacios")
public class EspacioController {

    private final EspacioService espacioService;

    public EspacioController(EspacioService espacioService) {
        this.espacioService = espacioService;
    }

    @PostMapping
    public Espacio registrar(@RequestBody Espacio espacio) {
        return espacioService.registrar(espacio);
    }

    @GetMapping("/disponibles")
    public List<Espacio> listarDisponibles() {
        return espacioService.listarDisponibles();
    }
    
    @PutMapping("/{id}/estado")
    public Espacio actualizarEstado(@PathVariable Long id, @RequestBody Espacio espacio) {
        return espacioService.actualizarEstado(id, espacio.getEstado());
    }
}