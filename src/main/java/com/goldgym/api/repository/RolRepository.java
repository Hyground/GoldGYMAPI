package com.goldgym.api.repository;

import com.goldgym.api.entities.Rol;

import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;



public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

}
