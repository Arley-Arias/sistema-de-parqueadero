package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.entity.Vehiculo;
import com.sistema.parqueadero.repository.VehiculoRepository;
import com.sistema.parqueadero.entity.Espacio;
import com.sistema.parqueadero.repository.EspacioRepository;
import com.sistema.parqueadero.entity.Movimiento;
import com.sistema.parqueadero.repository.MovimientoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
public class WebController {

    private final VehiculoRepository vehiculoRepository;
    private final EspacioRepository espacioRepository;
    private final MovimientoRepository movimientoRepository;

    public WebController(VehiculoRepository vehiculoRepository, EspacioRepository espacioRepository, MovimientoRepository movimientoRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.espacioRepository = espacioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    // --- VEHÍCULOS ---
    @GetMapping("/panel/vehiculos")
    public String mostrarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        model.addAttribute("nuevoVehiculo", new Vehiculo());
        return "vehiculos";
    }

    @PostMapping("/panel/vehiculos")
    public String guardarVehiculo(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
        return "redirect:/panel/vehiculos";
    }

    @GetMapping("/panel/vehiculos/eliminar/{placa}")
    public String eliminarVehiculo(@PathVariable("placa") String placa) {
        vehiculoRepository.deleteById(placa);
        return "redirect:/panel/vehiculos";
    }

    // --- ESPACIOS ---
    @GetMapping("/panel/espacios")
    public String mostrarEspacios(Model model) {
        model.addAttribute("espacios", espacioRepository.findAll());
        model.addAttribute("nuevoEspacio", new Espacio());
        return "espacios";
    }

    @PostMapping("/panel/espacios")
    public String guardarEspacio(Espacio espacio) {
        espacioRepository.save(espacio);
        return "redirect:/panel/espacios";
    }

    // --- MOVIMIENTOS ---
    @GetMapping("/panel/movimientos")
    public String mostrarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoRepository.findAll());
        model.addAttribute("nuevoMovimiento", new Movimiento());
        return "movimientos";
    }

    @PostMapping("/panel/movimientos")
    public String guardarMovimiento(Movimiento movimiento) {
        // Busca un espacio libre y lo ocupa
        Espacio espacioLibre = espacioRepository.findAll().stream()
                .filter(e -> e.getEstado().equals("DISPONIBLE"))
                .findFirst()
                .orElse(null);

        if (espacioLibre != null) {
            espacioLibre.setEstado("OCUPADO");
            espacioRepository.save(espacioLibre);
            
            movimiento.setEspacioAsignado(espacioLibre.getNumero());
            movimientoRepository.save(movimiento);
        }
        return "redirect:/panel/movimientos";
    }

    @GetMapping("/panel/movimientos/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        Movimiento mov = movimientoRepository.findById(id).orElse(null);
        
        if (mov != null && mov.getEstado().equals("ACTIVO")) {
            mov.setFechaSalida(LocalDateTime.now());
            mov.setEstado("FINALIZADO");

            // Cobrar $100 por minuto
            long minutos = Duration.between(mov.getFechaEntrada(), mov.getFechaSalida()).toMinutes();
            if (minutos == 0) minutos = 1; // Cobro mínimo de 1 minuto
            mov.setValorPagado((double) (minutos * 100));

            // Liberar el espacio
            Espacio espacio = espacioRepository.findAll().stream()
                    .filter(e -> e.getNumero().equals(mov.getEspacioAsignado()))
                    .findFirst()
                    .orElse(null);
            if (espacio != null) {
                espacio.setEstado("DISPONIBLE");
                espacioRepository.save(espacio);
            }
            movimientoRepository.save(mov);
        }
        return "redirect:/panel/movimientos";
    }
}