package com.goldgym.api.controllers;

import com.goldgym.api.entities.Venta;
import com.goldgym.api.repository.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard") // 👈 Importante: incluye /api si tus rutas públicas lo usan
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}) // 🔓 Permite llamadas desde tu frontend local
@AllArgsConstructor
public class DashboardEmpleadoController {

    private final VentaRepository ventaRepository;

    // =====================================================
    // 🧾 ENDPOINT: Actividad Reciente (para la tabla del dashboard)
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
    // 🧱 DTO INTERNO: Lo que se envía al frontend
    // =====================================================
    @Data
    @AllArgsConstructor
    public static class ActividadReciente {
        private String cliente;
        private String actividad;
        private LocalDateTime fecha;
    }

    // =====================================================
    // 🧪 ENDPOINT DE PRUEBA: Verificar conexión del dashboard
    // =====================================================
    @GetMapping("/test")
    public String test() {
        return "✅ API DashboardEmpleado funcionando correctamente";
    }
}
