package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evento_productos")
@Data
@NoArgsConstructor
public class EventoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoInventario producto;

    private Integer cantidadAsignada; // Cuántos ítems se llevó este evento

    public EventoProducto(Evento evento, ProductoInventario producto, Integer cantidadAsignada) {
        this.evento = evento;
        this.producto = producto;
        this.cantidadAsignada = cantidadAsignada;
    }
}