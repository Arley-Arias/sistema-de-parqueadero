package com.sistema.parqueadero.service;

import com.sistema.parqueadero.entity.Vehiculo;
import com.sistema.parqueadero.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public Vehiculo registrar(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }
}