package com.goldgym.api.repository;

import com.goldgym.api.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Devuelve las 5 ventas más recientes (ordena por fechaVenta descendente)
    List<Venta> findTop5ByOrderByFechaVentaDesc();
}
