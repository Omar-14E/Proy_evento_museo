package com.example.museo_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa un evento dentro del museo.
 * Contiene información sobre el nombre, descripción, fechas, horarios, tipo de
 * evento,
 * costo de entrada y la sala en la que se llevará a cabo el evento.
 */
@Entity
@Table(name = "Evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    private String nombre;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "tipo_evento")
    private String tipoEvento;

    @Column(name = "costo_entrada")
    private BigDecimal costoEntrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala")
    private Sala sala;

    /**
     * Constructor por defecto.
     */
    public Evento() {
    }

    /**
     * Obtiene el ID del evento.
     * 
     * @return El ID del evento.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del evento.
     * 
     * @param id El ID del evento.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del evento.
     * 
     * @return El nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     * 
     * @param nombre El nombre del evento.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del evento.
     * 
     * @return La descripción del evento.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del evento.
     * 
     * @param descripcion La descripción del evento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha de inicio del evento.
     * 
     * @return La fecha de inicio del evento.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del evento.
     * 
     * @param fechaInicio La fecha de inicio del evento.
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del evento.
     * 
     * @return La fecha de fin del evento.
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de fin del evento.
     * 
     * @param fechaFin La fecha de fin del evento.
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene la hora de inicio del evento.
     * 
     * @return La hora de inicio del evento.
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Establece la hora de inicio del evento.
     * 
     * @param horaInicio La hora de inicio del evento.
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Obtiene la hora de fin del evento.
     * 
     * @return La hora de fin del evento.
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Establece la hora de fin del evento.
     * 
     * @param horaFin La hora de fin del evento.
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Obtiene el tipo de evento.
     * 
     * @return El tipo de evento.
     */
    public String getTipoEvento() {
        return tipoEvento;
    }

    /**
     * Establece el tipo de evento.
     * 
     * @param tipoEvento El tipo de evento.
     */
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /**
     * Obtiene el costo de entrada al evento.
     * 
     * @return El costo de entrada al evento.
     */
    public BigDecimal getCostoEntrada() {
        return costoEntrada;
    }

    /**
     * Establece el costo de entrada al evento.
     * 
     * @param costoEntrada El costo de entrada al evento.
     */
    public void setCostoEntrada(BigDecimal costoEntrada) {
        this.costoEntrada = costoEntrada;
    }

    /**
     * Obtiene la sala en la que se llevará a cabo el evento.
     * 
     * @return La sala del evento.
     */
    public Sala getSala() {
        return sala;
    }

    /**
     * Establece la sala en la que se llevará a cabo el evento.
     * 
     * @param sala La sala del evento.
     */
    public void setSala(Sala sala) {
        this.sala = sala;
    }
}
