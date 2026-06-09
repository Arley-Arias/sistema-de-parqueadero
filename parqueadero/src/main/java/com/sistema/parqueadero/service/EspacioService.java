package com.sistema.parqueadero.service;

import com.sistema.parqueadero.entity.Espacio;
import com.sistema.parqueadero.repository.EspacioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EspacioService {

    private final EspacioRepository espacioRepository;

    public EspacioService(EspacioRepository espacioRepository) {
        this.espacioRepository = espacioRepository;
    }

    public Espacio registrar(Espacio espacio) {
        return espacioRepository.save(espacio);
    }

    public List<Espacio> listarDisponibles() {
        return espacioRepository.findByEstado("Disponible");
    }
    
    public Espacio actualizarEstado(Long id, String nuevoEstado) {
        Espacio espacio = espacioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Espacio no encontrado"));
        espacio.setEstado(nuevoEstado);
        return espacioRepository.save(espacio);
    }
}