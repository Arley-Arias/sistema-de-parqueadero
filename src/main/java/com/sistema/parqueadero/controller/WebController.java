package com.sistema.parqueadero.controller;

import com.sistema.parqueadero.dto.VehiculoDTO;
import com.sistema.parqueadero.service.VehiculoService;
import com.sistema.parqueadero.entity.Espacio;
import com.sistema.parqueadero.repository.EspacioRepository;
import com.sistema.parqueadero.entity.Movimiento;
import com.sistema.parqueadero.repository.MovimientoRepository;
import com.sistema.parqueadero.entity.Propietario;
import com.sistema.parqueadero.repository.PropietarioRepository;
import com.sistema.parqueadero.entity.Tarifa;
import com.sistema.parqueadero.repository.TarifaRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
public class WebController {

    private final VehiculoService vehiculoService;
    private final EspacioRepository espacioRepository;
    private final MovimientoRepository movimientoRepository;
    private final PropietarioRepository propietarioRepository;
    private final TarifaRepository tarifaRepository;

    public WebController(VehiculoService vehiculoService, 
                         EspacioRepository espacioRepository, 
                         MovimientoRepository movimientoRepository,
                         PropietarioRepository propietarioRepository,
                         TarifaRepository tarifaRepository) {
        this.vehiculoService = vehiculoService;
        this.espacioRepository = espacioRepository;
        this.movimientoRepository = movimientoRepository;
        this.propietarioRepository = propietarioRepository;
        this.tarifaRepository = tarifaRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // --- VEHÍCULOS (USANDO SERVICE Y DTO) ---
    @GetMapping("/panel/vehiculos")
    public String mostrarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("nuevoVehiculo", new VehiculoDTO());
        return "vehiculos";
    }

    @PostMapping("/panel/vehiculos")
    public String guardarVehiculo(VehiculoDTO vehiculoDTO) {
        vehiculoService.guardar(vehiculoDTO);
        return "redirect:/panel/vehiculos";
    }

    @GetMapping("/panel/vehiculos/eliminar/{placa}")
    public String eliminarVehiculo(@PathVariable("placa") String placa) {
        vehiculoService.eliminar(placa);
        return "redirect:/panel/vehiculos";
    }

    @GetMapping("/panel/vehiculos/editar/{placa}")
    public String editarVehiculo(@PathVariable String placa, Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("nuevoVehiculo", vehiculoService.buscarPorPlaca(placa));
        return "vehiculos";
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

    @GetMapping("/panel/espacios/mantenimiento/{id}")
    public String cambiarMantenimiento(@PathVariable Long id) {
        Espacio espacio = espacioRepository.findById(id).orElse(null);
        if (espacio != null) {
            if (espacio.getEstado().equals("DISPONIBLE")) {
                espacio.setEstado("MANTENIMIENTO");
            } else if (espacio.getEstado().equals("MANTENIMIENTO")) {
                espacio.setEstado("DISPONIBLE");
            }
            espacioRepository.save(espacio);
        }
        return "redirect:/panel/espacios";
    }

    // --- MOVIMIENTOS ---
    // --- MOVIMIENTOS ---
    @GetMapping("/panel/movimientos")
    public String mostrarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoRepository.findAll());
        model.addAttribute("nuevoMovimiento", new Movimiento());
        
        // Buscamos solo los espacios disponibles para mostrarlos en la lista
        var espaciosDisponibles = espacioRepository.findAll().stream()
                .filter(e -> e.getEstado().equals("DISPONIBLE"))
                .toList();
        model.addAttribute("espaciosDisponibles", espaciosDisponibles);
        
        return "movimientos";
    }

    @PostMapping("/panel/movimientos")
    public String guardarMovimiento(Movimiento movimiento) {
        // Buscamos el espacio específico que el operador seleccionó
        Espacio espacioSeleccionado = espacioRepository.findAll().stream()
                .filter(e -> e.getNumero().equals(movimiento.getEspacioAsignado()))
                .findFirst()
                .orElse(null);

        if (espacioSeleccionado != null && espacioSeleccionado.getEstado().equals("DISPONIBLE")) {
            espacioSeleccionado.setEstado("OCUPADO");
            espacioRepository.save(espacioSeleccionado);
            
            // Aseguramos que se guarde como activo
            movimiento.setEstado("ACTIVO");
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

            long minutos = Duration.between(mov.getFechaEntrada(), mov.getFechaSalida()).toMinutes();
            if (minutos == 0) minutos = 1; 

            Tarifa tarifaActual = tarifaRepository.findById(1L).orElse(new Tarifa());
            mov.setValorPagado((double) (minutos * tarifaActual.getValorMinuto()));

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

    // --- REPORTES ---
    @GetMapping("/panel/reportes")
    public String mostrarReportes(Model model) {
        var activos = movimientoRepository.findAll().stream()
                .filter(m -> m.getEstado().equals("ACTIVO"))
                .toList();

        double ingresos = movimientoRepository.findAll().stream()
                .filter(m -> m.getEstado().equals("FINALIZADO") && m.getValorPagado() != null)
                .mapToDouble(Movimiento::getValorPagado)
                .sum();

        model.addAttribute("activos", activos);
        model.addAttribute("ingresosTotales", ingresos);
        return "reportes";
    }

    // --- PROPIETARIOS ---
    @GetMapping("/panel/propietarios")
    public String mostrarPropietarios(Model model) {
        model.addAttribute("propietarios", propietarioRepository.findAll());
        model.addAttribute("nuevoPropietario", new Propietario());
        return "propietarios";
    }

    @PostMapping("/panel/propietarios")
    public String guardarPropietario(Propietario propietario) {
        propietarioRepository.save(propietario);
        return "redirect:/panel/propietarios";
    }

    @GetMapping("/panel/propietarios/eliminar/{documento}")
    public String eliminarPropietario(@PathVariable("documento") String documento) {
        propietarioRepository.deleteById(documento);
        return "redirect:/panel/propietarios";
    }

    @GetMapping("/panel/propietarios/editar/{documento}")
    public String editarPropietario(@PathVariable String documento, Model model) {
        model.addAttribute("propietarios", propietarioRepository.findAll());
        model.addAttribute("nuevoPropietario", propietarioRepository.findById(documento).orElse(new Propietario()));
        return "propietarios";
    }

    // --- CONFIGURACIÓN (SOLO ADMIN) ---
    @GetMapping("/panel/configuracion")
    public String mostrarConfiguracion(Model model) {
        Tarifa tarifa = tarifaRepository.findById(1L).orElse(new Tarifa());
        model.addAttribute("tarifa", tarifa);
        return "configuracion";
    }

    @PostMapping("/panel/configuracion")
    public String guardarConfiguracion(Tarifa tarifa) {
        tarifa.setId(1L); 
        tarifaRepository.save(tarifa);
        return "redirect:/panel/configuracion";
    }
}