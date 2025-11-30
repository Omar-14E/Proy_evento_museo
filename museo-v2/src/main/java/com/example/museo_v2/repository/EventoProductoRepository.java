package com.example.museo_v2.repository;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.EventoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para gestionar la relación entre eventos y productos asignados.
 * Permite consultar los productos asociados a un evento específico.
 */
public interface EventoProductoRepository extends JpaRepository<EventoProducto, Long> {

    /**
     * Obtiene la lista de productos asignados a un evento.
     *
     * @param evento evento del que se desean obtener los productos asignados
     * @return lista de {@link EventoProducto} asociados al evento
     */
    List<EventoProducto> findByEvento(Evento evento);
}
