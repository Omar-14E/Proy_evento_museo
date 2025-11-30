package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la relación entre un {@link Evento} y un {@link ProductoInventario},
 * indicando cuántas unidades de un producto fueron asignadas a un evento.
 */
@Entity
@Table(name = "evento_productos")
@Data
@NoArgsConstructor
public class EventoProducto {

    /** Identificador único del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Evento al que se asocia el producto. */
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    /** Producto asignado al evento. */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoInventario producto;

    /** Cantidad del producto asignada al evento. */
    private Integer cantidadAsignada;

    /**
     * Crea una nueva relación entre un evento y un producto.
     *
     * @param evento evento asociado
     * @param producto producto asignado
     * @param cantidadAsignada cantidad asignada al evento
     */
    public EventoProducto(Evento evento, ProductoInventario producto, Integer cantidadAsignada) {
        this.evento = evento;
        this.producto = producto;
        this.cantidadAsignada = cantidadAsignada;
    }
}
