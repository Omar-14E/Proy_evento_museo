package com.example.museo_v2.controller;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Reserva;
import com.example.museo_v2.service.EventoService;
import com.example.museo_v2.service.PdfService;
import com.example.museo_v2.service.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

/**
 * Controlador para la gestión de reservas de eventos.
 * Incluye registro de actividad y generación de tickets en PDF.
 */
@Controller
@RequestMapping("/reservas")
public class ReservaControlador {

    private static final Logger logger = LoggerFactory.getLogger(ReservaControlador.class);

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private PdfService pdfService;

    /**
     * Muestra el formulario para crear una reserva asociada a un evento.
     *
     * @param eventoId ID del evento a reservar
     * @param model modelo para enviar datos a la vista
     * @return vista del formulario de reserva
     */
    @GetMapping("/crear/{eventoId}")
    public String mostrarFormularioReserva(@PathVariable Long eventoId, Model model) {
        logger.info("ACCESO FORMULARIO | Solicitud de reserva para evento {}", eventoId);

        Evento evento = eventoService.obtenerEventoPorId(eventoId);
        if (evento == null) {
            logger.warn("EVENTO NO ENCONTRADO | ID {}", eventoId);
            return "redirect:/";
        }

        model.addAttribute("reserva", new Reserva());
        model.addAttribute("evento", evento);
        model.addAttribute("precioUnitario", evento.getCostoEntrada());

        return "reservas/formularioReserva";
    }

    /**
     * Procesa y guarda una reserva para un evento.
     *
     * @param eventoId ID del evento reservado
     * @param reserva datos de la reserva
     * @return redirección a la página de confirmación
     */
    @PostMapping("/guardar/{eventoId}")
    public String guardarReserva(@PathVariable Long eventoId, @ModelAttribute("reserva") Reserva reserva) {
        logger.info("RESERVA EN PROCESO | Evento {} | Cliente: {}", eventoId, reserva.getNombreCompleto());

        try {
            Reserva reservaGuardada = reservaService.guardarReserva(reserva, eventoId);

            logger.info("RESERVA EXITOSA | ID {} | Total S/ {} | Método {}",
                    reservaGuardada.getId(),
                    reservaGuardada.getTotalPagar(),
                    reservaGuardada.getMetodoPago());

            return "redirect:/reservas/confirmacion/" + reservaGuardada.getId();

        } catch (IllegalArgumentException e) {
            logger.error("ERROR EN RESERVA | Evento {} | Motivo: {}", eventoId, e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Muestra la página de confirmación de una reserva.
     *
     * @param reservaId ID de la reserva
     * @param model modelo con la información de la reserva
     * @return vista de confirmación
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
     * Redirección predeterminada cuando no se especifica reserva.
     *
     * @return redirección al inicio
     */
    @GetMapping("/confirmacion")
    public String mostrarConfirmacionBase() {
        return "redirect:/";
    }

    /**
     * Genera y descarga el ticket en PDF de una reserva.
     *
     * @param id ID de la reserva
     * @return archivo PDF como respuesta
     */
    @GetMapping("/ticket/{id}/pdf")
    public ResponseEntity<InputStreamResource> descargarTicket(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerReservaPorId(id);

        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream pdf = pdfService.generarTicketPdf(reserva);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_museo_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
