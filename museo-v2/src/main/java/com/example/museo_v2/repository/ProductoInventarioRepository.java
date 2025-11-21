package com.example.museo_v2.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.museo_v2.model.EstadoProducto;
import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.ProductoInventario;

import java.util.List;

public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {
    List<ProductoInventario> findByEstado(EstadoProducto estado);
    List<ProductoInventario> findByEventoAsignado(Evento evento);
}

