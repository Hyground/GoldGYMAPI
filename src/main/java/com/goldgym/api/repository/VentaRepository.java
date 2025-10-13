package com.goldgym.api.repository;

import com.goldgym.api.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findTop5ByOrderByFechaVentaDesc();

    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta = :fecha")
    BigDecimal sumTotalByFecha(LocalDate fecha);
}
