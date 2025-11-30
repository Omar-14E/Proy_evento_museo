package com.example.museo_v2.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa una sala dentro del museo. Contiene información sobre su
 * capacidad, ubicación, descripción y una imagen asociada.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sala")
@EqualsAndHashCode(of = "nombre")
public class Sala {

    /** Identificador único de la sala. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer id;

    /** Nombre de la sala. */
    private String nombre;

    /** Capacidad máxima de personas que puede albergar. */
    private Integer capacidad;

    /** Ubicación física dentro del museo. */
    private String ubicacion;

    /** Descripción general de la sala. */
    private String descripcion;

    /** URL de la imagen representativa de la sala. */
    @Column(name = "imagen_url")
    private String imagenUrl;

    /**
     * Constructor útil para crear instancias sin ID, como en pruebas de carga.
     *
     * @param nombre nombre de la sala
     * @param capacidad capacidad máxima
     * @param ubicacion ubicación dentro del museo
     * @param descripcion descripción de la sala
     * @param imagenUrl enlace a la imagen asociada
     */
    public Sala(String nombre, Integer capacidad, String ubicacion, String descripcion, String imagenUrl) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }
}
