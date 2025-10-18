package com.example.museo_v2.repository;

import com.example.museo_v2.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y manipular las reservas en la base de datos.
 * Extiende JpaRepository para proporcionar operaciones CRUD estándar.
 */
@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
    // No es necesario agregar métodos adicionales si solo se requieren operaciones CRUD básicas.
}
