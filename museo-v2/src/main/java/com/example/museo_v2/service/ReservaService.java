package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Reserva;
import com.example.museo_v2.repository.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Servicio encargado de la gestión de reservas, incluyendo el cálculo del total
 * a pagar y la persistencia de los registros en la base de datos.
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private EventoService eventoService;

    /**
     * Guarda una reserva asociada a un evento. Calcula el total a pagar
     * según el costo por entrada y la cantidad solicitada.
     *
     * @param reserva   reserva a registrar
     * @param eventoId  identificador del evento asociado
     * @return reserva guardada con el total a pagar calculado
     * @throws IllegalArgumentException si el evento no existe
     */
    public Reserva guardarReserva(Reserva reserva, Long eventoId) {

        Evento evento = eventoService.obtenerEventoPorId(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("Evento no encontrado.");
        }

        BigDecimal costoUnitario = evento.getCostoEntrada() != null
                ? evento.getCostoEntrada()
                : BigDecimal.ZERO;

        BigDecimal nEntradas = new BigDecimal(reserva.getNEntradas());
        BigDecimal total = costoUnitario.multiply(nEntradas).setScale(2, RoundingMode.HALF_UP);

        reserva.setEvento(evento);
        reserva.setTotalPagar(total);

        return reservaRepositorio.save(reserva);
    }

    /**
     * Obtiene una reserva por su identificador.
     *
     * @param id identificador de la reserva
     * @return la reserva encontrada o {@code null} si no existe
     */
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepositorio.findById(id).orElse(null);
    }
}
