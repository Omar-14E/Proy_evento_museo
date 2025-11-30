package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.EventoProducto;
import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.repository.EventoProductoRepository;
import com.example.museo_v2.repository.ProductoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Servicio para la gestión del inventario de productos, incluyendo consulta,
 * reserva y liberación de stock según los eventos asociados.
 */
@Service
public class ProductoInventarioService {

    @Autowired
    private ProductoInventarioRepository productoRepo;

    @Autowired
    private EventoProductoRepository eventoProductoRepo;

    /**
     * Obtiene todos los productos registrados en el inventario,
     * independientemente de su disponibilidad.
     *
     * @return lista completa de productos
     */
    public List<ProductoInventario> findAll() {
        return productoRepo.findAll();
    }

    /**
     * Obtiene únicamente los productos con stock disponible mayor a cero.
     *
     * @return lista de productos disponibles
     */
    public List<ProductoInventario> productosDisponibles() {
        return productoRepo.findByStockDisponibleGreaterThan(0);
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return el producto encontrado o {@code null} si no existe
     */
    public ProductoInventario findById(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

    /**
     * Guarda o actualiza un producto en el inventario.
     * Si se crea uno nuevo y no se especifica el stock disponible,
     * se asume que es igual al stock total.
     *
     * @param p producto a guardar
     * @return el producto persistido
     */
    public ProductoInventario guardar(ProductoInventario p) {
        if (p.getId() == null && p.getStockDisponible() == null) {
            p.setStockDisponible(p.getStockTotal());
        }
        return productoRepo.save(p);
    }

    /**
     * Reserva productos para un evento, descontando el stock disponible
     * y registrando la asignación en la tabla intermedia.
     *
     * @param evento evento para el cual se reservarán productos
     * @param requerimientos mapa donde la clave es el ID del producto
     *                       y el valor la cantidad solicitada
     */
    @Transactional
    public void reservarProductos(Evento evento, Map<Long, Integer> requerimientos) {
        for (Map.Entry<Long, Integer> entry : requerimientos.entrySet()) {
            Long prodId = entry.getKey();
            Integer cantidadSolicitada = entry.getValue();

            if (cantidadSolicitada > 0) {
                ProductoInventario producto = productoRepo.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + prodId));

                if (producto.getStockDisponible() < cantidadSolicitada) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }

                producto.setStockDisponible(producto.getStockDisponible() - cantidadSolicitada);
                productoRepo.save(producto);

                EventoProducto asignacion = new EventoProducto();
                asignacion.setEvento(evento);
                asignacion.setProducto(producto);
                asignacion.setCantidadAsignada(cantidadSolicitada);

                eventoProductoRepo.save(asignacion);
            }
        }
    }

    /**
     * Libera los productos asignados a un evento, devolviendo el stock
     * y eliminando los registros intermedios.
     *
     * @param evento evento del cual se liberarán los productos
     */
    @Transactional
    public void liberarProductos(Evento evento) {
        List<EventoProducto> asignaciones = eventoProductoRepo.findByEvento(evento);

        for (EventoProducto asignacion : asignaciones) {
            ProductoInventario producto = asignacion.getProducto();

            producto.setStockDisponible(producto.getStockDisponible() + asignacion.getCantidadAsignada());
            productoRepo.save(producto);

            eventoProductoRepo.delete(asignacion);
        }
    }
}
