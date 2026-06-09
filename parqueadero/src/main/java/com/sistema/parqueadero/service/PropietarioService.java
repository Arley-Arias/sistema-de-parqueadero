package com.sistema.parqueadero.service;

import com.sistema.parqueadero.entity.Propietario;
import com.sistema.parqueadero.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    public Propietario registrar(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    public List<Propietario> listar() {
        return propietarioRepository.findAll();
    }
}