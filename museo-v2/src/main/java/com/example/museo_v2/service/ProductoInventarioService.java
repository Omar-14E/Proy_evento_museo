package com.example.museo_v2.service;

import org.springframework.stereotype.Service;

import com.example.museo_v2.model.EstadoProducto;
import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.repository.ProductoInventarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ProductoInventarioService {

    @Autowired
    private ProductoInventarioRepository repo;

    public List<ProductoInventario> findAll() {
        return repo.findAll();
    }

    public List<ProductoInventario> productosDisponibles() {
        return repo.findByEstado(EstadoProducto.DISPONIBLE);
    }

    public ProductoInventario findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public ProductoInventario guardar(ProductoInventario producto) {
        return repo.save(producto);
    }

    public void asignarAEvento(List<Long> productoIds, Evento evento) {
        List<ProductoInventario> productos = repo.findAllById(productoIds);
        for (ProductoInventario producto : productos) {
            producto.setEstado(EstadoProducto.EN_USO);
            producto.setEventoAsignado(evento);
            repo.save(producto);
        }
    }

    public void liberarProductosDeEvento(Evento evento) {
        List<ProductoInventario> productos = repo.findByEventoAsignado(evento);
        for (ProductoInventario producto : productos) {
            producto.setEstado(EstadoProducto.DISPONIBLE);
            producto.setEventoAsignado(null);
            repo.save(producto);
        }
    }

    public void marcarDaniado(Long id) {
        ProductoInventario producto = repo.findById(id).orElse(null);
        if (producto != null) {
            producto.setEstado(EstadoProducto.DANADO);
            producto.setEventoAsignado(null);
            repo.save(producto);
        }
    }
    public List<ProductoInventario> productosPorEvento(Evento evento) {
        return repo.findByEventoAsignado(evento);
    }
}
