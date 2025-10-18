package com.example.museo_v2.controller;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Reserva;
import com.example.museo_v2.service.EventoService;
import com.example.museo_v2.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//Imports de logback
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para manejar las reservas de eventos en el museo.
 * Permite crear, guardar y confirmar reservas para eventos específicos.
 */
@Controller
@RequestMapping("/reservas")
public class ReservaControlador {
    //Instancia de logger
    private static final Logger logger = LoggerFactory.getLogger(ReservaControlador.class);

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EventoService eventoService;

    /**
     * Método que muestra el formulario para crear una nueva reserva.
     * 
     * @param eventoId El ID del evento para el cual se va a realizar la reserva.
     * @param model El modelo que contiene los atributos necesarios para la vista.
     * @return La vista del formulario de reserva.
     */
    @GetMapping("/crear/{eventoId}")
    public String mostrarFormularioReserva(@PathVariable Long eventoId, Model model) {

        logger.info("Solicitando formulario de reserva para evento ID: {}", eventoId);

        Evento evento = eventoService.obtenerEventoPorId(eventoId);
        if (evento == null) {
            logger.warn("Intento de reserva fallido: Evento ID {} no encontrado.", eventoId);
            return "redirect:/";
        }

        Reserva reserva = new Reserva();

        model.addAttribute("reserva", reserva);
        model.addAttribute("evento", evento);
        model.addAttribute("precioUnitario", evento.getCostoEntrada());

        return "reservas/formularioReserva";
    }

    /**
     * Método para procesar y guardar una nueva reserva.
     * 
     * @param eventoId El ID del evento para el cual se realiza la reserva.
     * @param reserva El objeto reserva que se va a guardar.
     * @return Redirige a la página de confirmación de la reserva.
     */
    @PostMapping("/guardar/{eventoId}")
    public String guardarReserva(@PathVariable Long eventoId, @ModelAttribute("reserva") Reserva reserva) {
        logger.info("Intentando guardar reserva para evento ID: {}. Datos: {}", eventoId, reserva);
        
        try {
            Reserva reservaGuardada = reservaService.guardarReserva(reserva, eventoId);
            logger.info("Reserva guardada exitosamente con ID: {}", reservaGuardada.getId());

            return "redirect:/reservas/confirmacion/" + reservaGuardada.getId();
        } catch (IllegalArgumentException e) {
            logger.error("Error al guardar la reserva para el evento ID: " + eventoId, e);
            
            return "redirect:/";
        }
    }

    /**
     * Método para mostrar la página de confirmación de la reserva.
     * 
     * @param reservaId El ID de la reserva que se desea confirmar.
     * @param model El modelo que contiene los atributos necesarios para la vista de confirmación.
     * @return La vista de confirmación de la reserva.
     */
    @GetMapping("/confirmacion/{reservaId}")
    public String mostrarConfirmacion(@PathVariable Long reservaId, Model model) {

        Reserva reserva = reservaService.obtenerReservaPorId(reservaId);

        if (reserva == null) {
            return "redirect:/";
        }

        model.addAttribute("reserva", reserva);

        return "reservas/confirmacionReserva";
    }

    /**
     * Redirige a la página de inicio si no se proporciona un ID de reserva para la confirmación.
     * 
     * @return Redirige a la página principal.
     */
    @GetMapping("/confirmacion")
    public String mostrarConfirmacionBase() {
        return "redirect:/";
    }
}
