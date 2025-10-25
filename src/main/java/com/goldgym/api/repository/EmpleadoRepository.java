package com.goldgym.api.repository;

import com.goldgym.api.entities.Empleado;
import com.goldgym.api.entities.Persona; // Importar si añades búsqueda por persona
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importar si añades búsqueda por persona


@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

     // Podrías añadir métodos de búsqueda específicos si los necesitas, por ejemplo:
     // Optional<Empleado> findByPersona(Persona persona);

}
