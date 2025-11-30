package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un producto dentro del inventario del museo.
 * Incluye información básica como nombre, tipo y control de stock.
 */
@Entity
@Table(name = "inventario_productos")
@Getter
@Setter
public class ProductoInventario {

    /** Identificador único del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre descriptivo del producto (ej.: “Silla de Plástico”). */
    private String nombre;

    /** Categoría o tipo del producto (ej.: “Mobiliario”, “Electrónico”). */
    private String tipo;

    /** Cantidad total del producto en inventario. */
    private Integer stockTotal;

    /** Cantidad actualmente disponible para asignar a eventos. */
    private Integer stockDisponible;
}
