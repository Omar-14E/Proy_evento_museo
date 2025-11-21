package com.example.museo_v2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario_productos")
public class ProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipo; // Ej: Proyector, Pizarra, Láser

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado = EstadoProducto.DISPONIBLE;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento eventoAsignado; // Relación con evento, nullable

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
    public Evento getEventoAsignado() { return eventoAsignado; }
    public void setEventoAsignado(Evento eventoAsignado) { this.eventoAsignado = eventoAsignado; }
}