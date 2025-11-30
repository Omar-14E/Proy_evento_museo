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

@ExtendWith(MockitoExtension.class)
public class ProductoInventarioServiceTest {

    @Mock
    private ProductoInventarioRepository productoRepo;

    @Mock
    private EventoProductoRepository eventoProductoRepo;

    @InjectMocks
    private ProductoInventarioService servicio;

    @Test
    void reservarProductos_StockSuficiente_DebeDescontarYGuardar() {
        // DATOS DE PRUEBA
        Evento evento = new Evento();
        evento.setId(1L);

        ProductoInventario producto = new ProductoInventario();
        producto.setId(10L);
        producto.setNombre("Proyector");
        producto.setStockDisponible(10); // Hay 10 disponibles

        Map<Long, Integer> requerimientos = new HashMap<>();
        requerimientos.put(10L, 3); // Quiero reservar 3

        // MOCKS
        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        // EJECUCIÓN
        servicio.reservarProductos(evento, requerimientos);

        // VERIFICACIÓN
        assertEquals(7, producto.getStockDisponible()); // 10 - 3 = 7
        verify(productoRepo).save(producto); // Se guardó el producto actualizado
        verify(eventoProductoRepo).save(any(EventoProducto.class)); // Se creó la relación
    }

    @Test
    void reservarProductos_StockInsuficiente_DebeLanzarExcepcion() {
        // DATOS DE PRUEBA
        ProductoInventario producto = new ProductoInventario();
        producto.setId(20L);
        producto.setStockDisponible(2); // Solo hay 2

        Map<Long, Integer> requerimientos = new HashMap<>();
        requerimientos.put(20L, 5); // Quiero 5 (No alcanza)

        // MOCKS
        when(productoRepo.findById(20L)).thenReturn(Optional.of(producto));

        // EJECUCIÓN Y VERIFICACIÓN
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicio.reservarProductos(new Evento(), requerimientos);
        });

        assertEquals("Stock insuficiente para: null", exception.getMessage()); // null porque no le puse nombre al producto mock
        verify(productoRepo, never()).save(any()); // No se debe guardar nada
    }

    @Test
    void liberarProductos_DebeRestaurarStock() {
        // DATOS DE PRUEBA
        Evento evento = new Evento();
        
        ProductoInventario producto = new ProductoInventario();
        producto.setStockDisponible(5);

        EventoProducto asignacion = new EventoProducto();
        asignacion.setProducto(producto);
        asignacion.setCantidadAsignada(3); // Este evento tenía 3 reservados

        // MOCKS
        when(eventoProductoRepo.findByEvento(evento)).thenReturn(List.of(asignacion));

        // EJECUCIÓN
        servicio.liberarProductos(evento);

        // VERIFICACIÓN
        assertEquals(8, producto.getStockDisponible()); // 5 + 3 = 8
        verify(productoRepo).save(producto); // Se actualizó el stock
        verify(eventoProductoRepo).delete(asignacion); // Se borró la asignación
    }
}