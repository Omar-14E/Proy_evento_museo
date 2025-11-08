package com.example.museo_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Representa una reserva realizada para un evento en el museo.
 * Contiene información sobre el cliente, el evento, las entradas, el pago, y el
 * tipo de comprobante.
 */
@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "dniRuc")
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String dniRuc;

    private String telefono;

    @Column(nullable = false)
    private Integer nEntradas;

    private BigDecimal totalPagar;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    private String tipoComprobante;
    private String metodoPago;
    private LocalDateTime fechaReserva = LocalDateTime.now();

}