package com.example.museo_v2.repository;

import com.example.museo_v2.model.ProductoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {

    List<ProductoInventario> findByStockDisponibleGreaterThan(Integer cantidad);
    long countByStockDisponibleLessThan(Integer cantidadLimite);

}