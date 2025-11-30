package com.example.museo_v2.repository;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.EventoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoProductoRepository extends JpaRepository<EventoProducto, Long> {
    List<EventoProducto> findByEvento(Evento evento);
}