package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Reserva;
import com.example.museo_v2.repository.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Servicio encargado de la lógica de negocio relacionada con las reservas.
 * Incluye métodos para guardar reservas y calcular el total a pagar.
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private EventoService eventoService;

    /**
     * Guarda una reserva en la base de datos.
     * Calcula el total a pagar por la reserva basándose en el número de entradas y el costo por entrada.
     * 
     * @param reserva La reserva que se va a guardar.
     * @param eventoId El ID del evento al que se asocia la reserva.
     * @return La reserva guardada con el total a pagar calculado.
     * @throws IllegalArgumentException Si el evento no existe en la base de datos.
     */
    public Reserva guardarReserva(Reserva reserva, Long eventoId) {

        // Obtener el evento asociado a la reserva
        Evento evento = eventoService.obtenerEventoPorId(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("Evento no encontrado.");
        }

        // Obtener el costo unitario de la entrada
        BigDecimal costoUnitario = evento.getCostoEntrada() != null
                ? evento.getCostoEntrada()
                : BigDecimal.ZERO;

        // Obtener el número de entradas
        BigDecimal nEntradas = new BigDecimal(reserva.getNEntradas());

        // Calcular el total a pagar
        BigDecimal total = costoUnitario.multiply(nEntradas);

        // Redondear el total a dos decimales
        total = total.setScale(2, RoundingMode.HALF_UP);

        // Establecer el evento y el total a pagar en la reserva
        reserva.setEvento(evento);
        reserva.setTotalPagar(total);

        // Guardar la reserva y devolverla
        return reservaRepositorio.save(reserva);
    }

    /**
     * Obtiene una reserva por su ID.
     * 
     * @param id El ID de la reserva a obtener.
     * @return La reserva correspondiente al ID proporcionado o null si no se encuentra.
     */
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepositorio.findById(id).orElse(null);
    }
}
