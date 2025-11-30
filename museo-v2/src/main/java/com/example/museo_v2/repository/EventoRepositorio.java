package com.example.museo_v2.repository;

import com.example.museo_v2.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para acceder y manipular los eventos en la base de datos.
 * Extiende JpaRepository para proporcionar operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface EventoRepositorio extends JpaRepository<Evento, Long> {

    /**
     * Obtiene los eventos del mes actual, ordenados por fecha de inicio.
     * 
     * @param currentDate La fecha actual para filtrar los eventos del mes.
     * @return Una lista de eventos que ocurren en el mismo mes y año que la fecha proporcionada.
     */
    @Query("SELECT e FROM Evento e WHERE YEAR(e.fechaInicio) = YEAR(?1) AND MONTH(e.fechaInicio) = MONTH(?1) ORDER BY e.fechaInicio ASC")
    List<Evento> findEventosByMonthAndYear(LocalDate currentDate);

    /**
     * Obtiene los eventos futuros, es decir, aquellos cuyo inicio es posterior a la fecha proporcionada.
     * 
     * @param date La fecha límite para filtrar eventos que ocurren después de esta fecha.
     * @return Una lista de eventos futuros ordenados por fecha de inicio.
     */
    List<Evento> findByFechaInicioAfterOrderByFechaInicioAsc(LocalDate date);

    long countByFechaInicioAfter(LocalDate fecha);
}
