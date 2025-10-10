package com.goldgym.api.repository;

import com.goldgym.api.entities.Cliente;

import com.goldgym.api.entities.Pago;

import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;



public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findTopByClienteOrderByFechaVencimientoDesc(Cliente cliente);

}
