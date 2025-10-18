package com.example.museo_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una reserva realizada para un evento en el museo.
 * Contiene información sobre el cliente, el evento, las entradas, el pago, y el tipo de comprobante.
 */
@Entity
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

    /**
     * Constructor por defecto.
     */
    public Reserva() {
    }

    /**
     * Obtiene el ID de la reserva.
     * 
     * @return El ID de la reserva.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la reserva.
     * 
     * @param id El ID de la reserva.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre completo del cliente que realizó la reserva.
     * 
     * @return El nombre completo del cliente.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el nombre completo del cliente que realizó la reserva.
     * 
     * @param nombreCompleto El nombre completo del cliente.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Obtiene el DNI o RUC del cliente que realizó la reserva.
     * 
     * @return El DNI o RUC del cliente.
     */
    public String getDniRuc() {
        return dniRuc;
    }

    /**
     * Establece el DNI o RUC del cliente que realizó la reserva.
     * 
     * @param dniRuc El DNI o RUC del cliente.
     */
    public void setDniRuc(String dniRuc) {
        this.dniRuc = dniRuc;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * 
     * @return El número de teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * 
     * @param telefono El número de teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el número de entradas reservadas.
     * 
     * @return El número de entradas reservadas.
     */
    public Integer getNEntradas() {
        return nEntradas;
    }

    /**
     * Establece el número de entradas reservadas.
     * 
     * @param nEntradas El número de entradas reservadas.
     */
    public void setNEntradas(Integer nEntradas) {
        this.nEntradas = nEntradas;
    }

    /**
     * Obtiene el total a pagar por la reserva.
     * 
     * @return El total a pagar por la reserva.
     */
    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    /**
     * Establece el total a pagar por la reserva.
     * 
     * @param totalPagar El total a pagar por la reserva.
     */
    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    /**
     * Obtiene el evento asociado con la reserva.
     * 
     * @return El evento asociado a la reserva.
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Establece el evento asociado con la reserva.
     * 
     * @param evento El evento asociado a la reserva.
     */
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    /**
     * Obtiene el tipo de comprobante para la reserva.
     * 
     * @return El tipo de comprobante para la reserva (por ejemplo, factura, boleta).
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Establece el tipo de comprobante para la reserva.
     * 
     * @param tipoComprobante El tipo de comprobante para la reserva.
     */
    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * Obtiene el método de pago utilizado para la reserva.
     * 
     * @return El método de pago utilizado para la reserva (por ejemplo, tarjeta de crédito, efectivo).
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado para la reserva.
     * 
     * @param metodoPago El método de pago utilizado para la reserva.
     */
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la reserva.
     * 
     * @return La fecha y hora de la reserva.
     */
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Establece la fecha y hora en que se realizó la reserva.
     * 
     * @param fechaReserva La fecha y hora de la reserva.
     */
    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}
