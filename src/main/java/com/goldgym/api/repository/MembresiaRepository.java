package com.goldgym.api.repository;

import com.goldgym.api.domain.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MembresiaRepository extends JpaRepository<Membresia, UUID> { }
