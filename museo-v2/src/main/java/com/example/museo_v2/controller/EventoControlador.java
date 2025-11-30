package com.example.museo_v2.controller;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.service.EventoService;
import com.example.museo_v2.service.ExcelService;
import com.example.museo_v2.service.ProductoInventarioService;
import com.example.museo_v2.service.SalaService;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de eventos del museo.
 * Maneja operaciones de creación, edición, eliminación, inventario
 * y exportación de reportes.
 */
@Controller
@RequestMapping("/eventos")
public class EventoControlador {

    private static final Logger logger = LoggerFactory.getLogger(EventoControlador.class);

    @Autowired
    private EventoService eventoService;

    @Autowired
    private SalaService salaService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ProductoInventarioService productoInventarioService;

    /**
     * Muestra la lista de eventos registrados.
     *
     * @param modelo modelo para enviar datos a la vista
     * @return vista de lista de eventos
     */
    @GetMapping
    public String listarEventos(Model modelo) {
        modelo.addAttribute("eventos", eventoService.listarTodosLosEventos());
        return "eventos/listaEventos";
    }

    /**
     * Muestra el formulario para registrar un nuevo evento.
     *
     * @param modelo modelo con datos iniciales para la vista
     * @return vista del formulario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("evento", new Evento());
        modelo.addAttribute("salas", salaService.listarSalasDisponibles());
        modelo.addAttribute("productosDisponibles", productoInventarioService.productosDisponibles());
        return "eventos/formularioEvento";
    }

    /**
     * Guarda un evento y procesa la reserva de productos en inventario.
     *
     * @param evento       datos del evento a guardar
     * @param productoIds  IDs de productos seleccionados
     * @param cantidades   cantidades requeridas de cada producto
     * @param modelo       modelo para mostrar errores en la vista
     * @return redirección o vista del formulario en caso de error
     */
    @PostMapping("/guardar")
    public String guardarEvento(@ModelAttribute Evento evento,
                                @RequestParam(required = false) List<Long> productoIds,
                                @RequestParam(required = false) List<Integer> cantidades,
                                Model modelo) {

        try {
            Preconditions.checkNotNull(evento, "El evento no puede ser nulo");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(evento.getNombre()), "El nombre no puede estar vacío");
            Preconditions.checkNotNull(evento.getSala(), "Debe asignarse una sala");

            Evento eventoGuardado = eventoService.guardarEvento(evento);

            logger.info("EVENTO GUARDADO | ID: {} | Nombre: {} | Sala: {}",
                    eventoGuardado.getId(),
                    eventoGuardado.getNombre(),
                    eventoGuardado.getSala().getNombre());

            if (productoIds != null && cantidades != null && !productoIds.isEmpty()) {
                Map<Long, Integer> requerimientos = new HashMap<>();

                for (int i = 0; i < productoIds.size(); i++) {
                    if (i < cantidades.size() && cantidades.get(i) != null && cantidades.get(i) > 0) {
                        requerimientos.put(productoIds.get(i), cantidades.get(i));
                    }
                }

                if (!requerimientos.isEmpty()) {
                    productoInventarioService.reservarProductos(eventoGuardado, requerimientos);
                    logger.info("INVENTARIO RESERVADO | Evento: {} | Items: {}", 
                                eventoGuardado.getNombre(), requerimientos.size());
                }
            }

            return "redirect:/eventos";

        } catch (RuntimeException e) {
            logger.warn("ERROR AL GUARDAR EVENTO: {}", e.getMessage());

            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("evento", evento);
            modelo.addAttribute("salas", salaService.listarSalasDisponibles());
            modelo.addAttribute("productosDisponibles", productoInventarioService.productosDisponibles());

            return "eventos/formularioEvento";
        }
    }

    /**
     * Muestra el formulario para editar un evento existente.
     *
     * @param id     identificador del evento
     * @param modelo modelo con datos del evento
     * @return vista del formulario
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model modelo) {
        Evento evento = eventoService.obtenerEventoPorId(id);
        if (evento == null) throw new IllegalArgumentException("ID inválido: " + id);

        modelo.addAttribute("evento", evento);
        modelo.addAttribute("salas", salaService.listarSalasDisponibles());
        modelo.addAttribute("productosDisponibles", productoInventarioService.productosDisponibles());
        return "eventos/formularioEvento";
    }

    /**
     * Elimina un evento y libera el stock de productos reservados.
     *
     * @param id identificador del evento
     * @return redirección a la lista de eventos
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        Evento evento = eventoService.obtenerEventoPorId(id);

        if (evento != null) {
            productoInventarioService.liberarProductos(evento);
            logger.info("STOCK RESTAURADO | Evento ID: {}", id);

            eventoService.eliminarEvento(id);
            logger.info("EVENTO ELIMINADO | ID: {}", id);
        }

        return "redirect:/eventos";
    }

    /**
     * Exporta la lista de eventos a un archivo Excel.
     *
     * @return archivo Excel como recurso descargable
     */
    @GetMapping("/exportar/excel")
    public ResponseEntity<Resource> exportarEventosExcel() {
        List<Evento> eventos = eventoService.listarTodosLosEventos();
        ByteArrayInputStream excelFile = excelService.crearExcelDeEventos(eventos);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_eventos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}
