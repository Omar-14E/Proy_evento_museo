package com.example.museo_v2.model;

import jakarta.persistence.*;

/**
 * Representa una sala dentro del museo.
 * Contiene información sobre el nombre, capacidad, ubicación, descripción e imagen de la sala.
 */
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
     * Constructor por defecto.
     */
    public Sala() {
    }

    /**
     * Constructor para inicializar una sala con los datos especificados.
     * 
     * @param nombre El nombre de la sala.
     * @param capacidad La capacidad de la sala.
     * @param ubicacion La ubicación de la sala dentro del museo.
     * @param descripcion Una breve descripción de la sala.
     * @param imagenUrl La URL de la imagen asociada a la sala.
     */
    public Sala(String nombre, Integer capacidad, String ubicacion, String descripcion, String imagenUrl) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    /**
     * Obtiene el ID de la sala.
     * 
     * @return El ID de la sala.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la sala.
     * 
     * @param id El ID de la sala.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la sala.
     * 
     * @return El nombre de la sala.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la sala.
     * 
     * @param nombre El nombre de la sala.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la capacidad de la sala.
     * 
     * @return La capacidad de la sala.
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * Establece la capacidad de la sala.
     * 
     * @param capacidad La capacidad de la sala.
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Obtiene la ubicación de la sala dentro del museo.
     * 
     * @return La ubicación de la sala.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación de la sala dentro del museo.
     * 
     * @param ubicacion La ubicación de la sala.
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la descripción de la sala.
     * 
     * @return La descripción de la sala.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la sala.
     * 
     * @param descripcion La descripción de la sala.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la URL de la imagen asociada a la sala.
     * 
     * @return La URL de la imagen asociada a la sala.
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen asociada a la sala.
     * 
     * @param imagenUrl La URL de la imagen asociada a la sala.
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
