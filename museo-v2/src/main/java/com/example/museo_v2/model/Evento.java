package com.example.museo_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Representa un evento dentro del museo.
 * Contiene información sobre el nombre, descripción, fechas, horarios, tipo de
 * evento,
 * costo de entrada y la sala en la que se llevará a cabo el evento.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nombre")
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

}