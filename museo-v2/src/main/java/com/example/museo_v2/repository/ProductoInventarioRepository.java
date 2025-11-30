package com.example.museo_v2.repository;

import com.example.museo_v2.model.ProductoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para acceder y gestionar los productos del inventario.
 * Incluye consultas relacionadas con disponibilidad y control de stock.
 */
public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {

    /**
     * Obtiene todos los productos cuyo stock disponible sea mayor
     * que el valor indicado.
     *
     * @param cantidad valor mínimo de stock disponible
     * @return lista de productos con stock superior al indicado
     */
    List<ProductoInventario> findByStockDisponibleGreaterThan(Integer cantidad);

    /**
     * Cuenta los productos que tienen un stock disponible inferior
     * al límite especificado.
     *
     * @param cantidadLimite valor máximo de stock disponible
     * @return cantidad de productos que están por debajo del límite
     */
    long countByStockDisponibleLessThan(Integer cantidadLimite);
}
