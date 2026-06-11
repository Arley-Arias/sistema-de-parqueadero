package com.sistema.parqueadero.service;

import com.sistema.parqueadero.dto.VehiculoDTO;
import com.sistema.parqueadero.entity.Vehiculo;
import com.sistema.parqueadero.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    private final VehiculoRepository repo;

    public VehiculoService(VehiculoRepository repo) {
        this.repo = repo;
    }

    public List<VehiculoDTO> listarTodos() {
        return repo.findAll().stream().map(this::convertirADto).collect(Collectors.toList());
    }

    public void guardar(VehiculoDTO dto) {
        Vehiculo v = new Vehiculo();
        v.setPlaca(dto.getPlaca());
        v.setTipo(dto.getTipo());
        v.setMarca(dto.getMarca());
        v.setNombrePropietario(dto.getNombrePropietario());
        repo.save(v);
    }

    public void eliminar(String placa) {
        repo.deleteById(placa);
    }

    public VehiculoDTO buscarPorPlaca(String placa) {
        return repo.findById(placa).map(this::convertirADto).orElse(new VehiculoDTO());
    }

    private VehiculoDTO convertirADto(Vehiculo v) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setPlaca(v.getPlaca());
        dto.setTipo(v.getTipo());
        dto.setMarca(v.getMarca());
        dto.setNombrePropietario(v.getNombrePropietario());
        return dto;
    }
}