package com.goldgym.api.controllers;

import com.goldgym.api.entities.Venta;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.MembresiaRepository;
import com.goldgym.api.repository.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@AllArgsConstructor
public class DashboardEmpleadoController {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;
    private final MembresiaRepository membresiaRepository;

    // =====================================================
    // 🧾 ENDPOINT: Actividad Reciente
    // =====================================================
    @GetMapping("/actividad")
    public List<ActividadReciente> getActividadReciente() {
        return ventaRepository.findTop5ByOrderByFechaVentaDesc()
                .stream()
                .map(v -> new ActividadReciente(
                        obtenerNombreCliente(v),
                        "Compra realizada",
                        v.getFechaVenta()
                ))
                .collect(Collectors.toList());
    }

    // =====================================================
    // 📊 ENDPOINT: Resumen general del dashboard
    // =====================================================
    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        // 🟢 Clientes activos
        long clientesActivos = clienteRepository.countByActivoTrue();

        // 🟡 Ventas del día
        BigDecimal ventasDelDia = ventaRepository.sumTotalByFecha(LocalDate.now());
        if (ventasDelDia == null) ventasDelDia = BigDecimal.ZERO;

        // 🔴 Membresías vencidas
        long membresiasVencidas = membresiaRepository.countByFechaFinBefore(LocalDate.now());

        return Map.of(
                "clientesActivos", clientesActivos,
                "ventasDelDia", ventasDelDia,
                "membresiasVencidas", membresiasVencidas
        );
    }

    // =====================================================
    // 💡 MÉTODO AUXILIAR: Obtener nombre completo del cliente
    // =====================================================
    private String obtenerNombreCliente(Venta v) {
        try {
            if (v.getCliente() != null && v.getCliente().getPersona() != null) {
                return v.getCliente().getPersona().getNombre() + " " + v.getCliente().getPersona().getApellido();
            } else {
                return "Cliente desconocido";
            }
        } catch (Exception e) {
            return "Cliente desconocido";
        }
    }

    // =====================================================
    // 🧱 DTO INTERNO: Actividad Reciente
    // =====================================================
    @Data
    @AllArgsConstructor
    public static class ActividadReciente {
        private String cliente;
        private String actividad;
        private LocalDateTime fecha;
    }

    // =====================================================
    // 🧪 ENDPOINT DE PRUEBA
    // =====================================================
    @GetMapping("/test")
    public String test() {
        return "✅ API DashboardEmpleado funcionando correctamente";
    }
}
