package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "Sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer id;

    private String nombre;
    private Integer capacidad;
    private String ubicacion;
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    /**
     * Constructor para inicializar una sala
     * Usado en el test de Excel.
     */
    public Sala(String nombre, Integer capacidad, String ubicacion, String descripcion, String imagenUrl) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

}