package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.EventoProducto;
import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.repository.EventoProductoRepository;
import com.example.museo_v2.repository.ProductoInventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link ProductoInventarioService}.
 * Valida la asignación y liberación de stock al gestionar productos
 * asociados a eventos.
 */
@ExtendWith(MockitoExtension.class)
public class ProductoInventarioServiceTest {

    @Mock
    private ProductoInventarioRepository productoRepo;

    @Mock
    private EventoProductoRepository eventoProductoRepo;

    @InjectMocks
    private ProductoInventarioService servicio;

    /**
     * Verifica que cuando hay stock suficiente, el servicio descuente la cantidad solicitada
     * y registre la relación Evento-Producto correctamente.
     */
    @Test
    void reservarProductos_StockSuficiente_DebeDescontarYGuardar() {
        Evento evento = new Evento();
        evento.setId(1L);

        ProductoInventario producto = new ProductoInventario();
        producto.setId(10L);
        producto.setNombre("Proyector");
        producto.setStockDisponible(10);

        Map<Long, Integer> requerimientos = new HashMap<>();
        requerimientos.put(10L, 3);

        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        servicio.reservarProductos(evento, requerimientos);

        assertEquals(7, producto.getStockDisponible());
        verify(productoRepo).save(producto);
        verify(eventoProductoRepo).save(any(EventoProducto.class));
    }

    /**
     * Verifica que el método lance una excepción si se intenta reservar más stock del disponible.
     */
    @Test
    void reservarProductos_StockInsuficiente_DebeLanzarExcepcion() {
        ProductoInventario producto = new ProductoInventario();
        producto.setId(20L);
        producto.setStockDisponible(2);

        Map<Long, Integer> requerimientos = new HashMap<>();
        requerimientos.put(20L, 5);

        when(productoRepo.findById(20L)).thenReturn(Optional.of(producto));

        Exception exception = assertThrows(RuntimeException.class, () ->
                servicio.reservarProductos(new Evento(), requerimientos)
        );

        assertEquals("Stock insuficiente para: null", exception.getMessage());
        verify(productoRepo, never()).save(any());
    }

    /**
     * Verifica que al liberar productos, el stock se restaure y se elimine
     * la asignación correspondiente.
     */
    @Test
    void liberarProductos_DebeRestaurarStock() {
        Evento evento = new Evento();

        ProductoInventario producto = new ProductoInventario();
        producto.setStockDisponible(5);

        EventoProducto asignacion = new EventoProducto();
        asignacion.setProducto(producto);
        asignacion.setCantidadAsignada(3);

        when(eventoProductoRepo.findByEvento(evento)).thenReturn(List.of(asignacion));

        servicio.liberarProductos(evento);

        assertEquals(8, producto.getStockDisponible());
        verify(productoRepo).save(producto);
        verify(eventoProductoRepo).delete(asignacion);
    }
}
