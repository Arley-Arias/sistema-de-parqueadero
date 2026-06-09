package com.sistema.parqueadero.service;

import com.sistema.parqueadero.entity.*;
import com.sistema.parqueadero.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Duration;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final EspacioRepository espacioRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, VehiculoRepository vehiculoRepository, EspacioRepository espacioRepository, UsuarioRepository usuarioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.espacioRepository = espacioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Movimiento registrarIngreso(String placa, Long idUsuario) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Espacio> disponibles = espacioRepository.findByEstado("Disponible");
        if (disponibles.isEmpty()) {
            throw new RuntimeException("No hay espacios disponibles en el parqueadero");
        }
        
        // Asignar el primer espacio libre automáticamente
        Espacio espacioAsignado = disponibles.get(0);
        espacioAsignado.setEstado("Ocupado");
        espacioRepository.save(espacioAsignado);

        Movimiento movimiento = new Movimiento();
        movimiento.setVehiculo(vehiculo);
        movimiento.setUsuario(usuario);
        movimiento.setEspacio(espacioAsignado);
        movimiento.setFechaIngreso(LocalDateTime.now());

        return movimientoRepository.save(movimiento);
    }

    public Movimiento registrarSalida(Long idMovimiento) {
        Movimiento movimiento = movimientoRepository.findById(idMovimiento)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        if (movimiento.getFechaSalida() != null) {
            throw new RuntimeException("El vehículo ya registró salida previamente");
        }

        movimiento.setFechaSalida(LocalDateTime.now());
        
        // Calcular tiempo
        long minutos = Duration.between(movimiento.getFechaIngreso(), movimiento.getFechaSalida()).toMinutes();
        movimiento.setTiempoMinutos((int) minutos);
        
        // Tarifa de ejemplo: $100 por minuto
        BigDecimal tarifaPorMinuto = new BigDecimal("100.00");
        movimiento.setValorTotal(tarifaPorMinuto.multiply(new BigDecimal(minutos)));

        // Liberar espacio
        Espacio espacio = movimiento.getEspacio();
        espacio.setEstado("Disponible");
        espacioRepository.save(espacio);

        return movimientoRepository.save(movimiento);
    }
}