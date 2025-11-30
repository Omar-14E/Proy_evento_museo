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

@Service
public class ProductoInventarioService {

    @Autowired
    private ProductoInventarioRepository productoRepo;

    @Autowired
    private EventoProductoRepository eventoProductoRepo;

    // Lista todo (incluso lo que no tiene stock)
    public List<ProductoInventario> findAll() {
        return productoRepo.findAll();
    }

    // --- EL MÉTODO QUE FALTABA ---
    // Devuelve solo los productos que tienen stock disponible > 0
    public List<ProductoInventario> productosDisponibles() {
        return productoRepo.findByStockDisponibleGreaterThan(0);
    }

    public ProductoInventario findById(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

    public ProductoInventario guardar(ProductoInventario p) {
        // Al crear uno nuevo, si no se especifica disponible, es igual al total
        if (p.getId() == null && p.getStockDisponible() == null) {
            p.setStockDisponible(p.getStockTotal());
        }
        return productoRepo.save(p);
    }

    // Método transaccional para descontar stock
    @Transactional
    public void reservarProductos(Evento evento, Map<Long, Integer> requerimientos) {
        for (Map.Entry<Long, Integer> entry : requerimientos.entrySet()) {
            Long prodId = entry.getKey();
            Integer cantidadSolicitada = entry.getValue();

            if (cantidadSolicitada > 0) {
                ProductoInventario producto = productoRepo.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + prodId));

                // Validar Stock
                if (producto.getStockDisponible() < cantidadSolicitada) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }

                // Descontar Stock
                producto.setStockDisponible(producto.getStockDisponible() - cantidadSolicitada);
                productoRepo.save(producto);

                // Registrar relación en tabla intermedia
                EventoProducto asignacion = new EventoProducto();
                asignacion.setEvento(evento);
                asignacion.setProducto(producto);
                asignacion.setCantidadAsignada(cantidadSolicitada);
                
                eventoProductoRepo.save(asignacion);
            }
        }
    }

    // Método para devolver stock al eliminar un evento
    @Transactional
    public void liberarProductos(Evento evento) {
        List<EventoProducto> asignaciones = eventoProductoRepo.findByEvento(evento);

        for (EventoProducto asignacion : asignaciones) {
            ProductoInventario producto = asignacion.getProducto();
            
            // Devolver cantidad al stock
            producto.setStockDisponible(producto.getStockDisponible() + asignacion.getCantidadAsignada());
            productoRepo.save(producto);

            // Borrar registro de la tabla intermedia
            eventoProductoRepo.delete(asignacion);
        }
    }
}