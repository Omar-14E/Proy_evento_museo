package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventario_productos")
@Getter @Setter
public class ProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: Silla de Plástico, Proyector Epson
    private String tipo;   // Ej: Mobiliario, Electrónico

    // Nuevo: Control de Stock
    private Integer stockTotal;      // Ej: 100 sillas en total
    private Integer stockDisponible; // Ej: 80 sillas disponibles actualmente

    // Eliminamos 'eventoAsignado' y 'estado' simple, ya que el stock varía dinámicamente
    // Si prefieres mantener 'estado' para saber si el producto está 'ACTIVO' o 'DESCONTINUADO', puedes dejarlo.
}