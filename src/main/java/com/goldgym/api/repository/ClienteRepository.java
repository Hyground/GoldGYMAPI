package com.goldgym.api.repository;

import com.goldgym.api.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> { }
