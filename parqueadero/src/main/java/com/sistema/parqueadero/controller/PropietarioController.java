package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Propietario;
import com.sistema.parqueadero.service.PropietarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    @PostMapping
    public Propietario registrar(@RequestBody Propietario propietario) {
        return propietarioService.registrar(propietario);
    }

    @GetMapping
    public List<Propietario> listar() {
        return propietarioService.listar();
    }
}
