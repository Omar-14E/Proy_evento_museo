package com.example.museo_v2.controller;


import com.example.museo_v2.model.Evento;

import com.example.museo_v2.service.EventoService;
import com.example.museo_v2.service.ExcelService;
import com.example.museo_v2.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import java.util.List; 

// Imports necesarios de Guava
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
/**
 * Controlador que maneja las operaciones relacionadas con los eventos del museo.
 * Permite listar, agregar, editar y eliminar eventos.
 */
@Controller
@RequestMapping("/eventos")
public class EventoControlador {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private SalaService salaService;
    
    @Autowired
    private ExcelService excelService;

    /**
     * Método para listar todos los eventos registrados en el museo.
     * 
     * @param modelo El modelo que contiene los atributos necesarios para la vista.
     * @return La vista de la lista de eventos.
     */
    @GetMapping
    public String listarEventos(Model modelo) {
        modelo.addAttribute("eventos", eventoService.listarTodosLosEventos());
        return "eventos/listaEventos";
    }

    /**
     * Método que muestra el formulario para registrar un nuevo evento.
     * 
     * @param modelo El modelo que contiene los atributos necesarios para el formulario.
     * @return La vista del formulario para registrar un evento.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("evento", new Evento());
        modelo.addAttribute("salas", salaService.listarSalasDisponibles());
        return "eventos/formularioEvento";
    }

    /**
     * Método para guardar un nuevo evento en la base de datos.
     * 
     * @param evento El evento que se va a guardar.
     * @return Redirige a la lista de eventos después de guardar el evento.
     */
    @PostMapping("/guardar")
    public String guardarEvento(@ModelAttribute("evento") Evento evento) {
        Preconditions.checkNotNull(evento, "El objeto Evento no puede ser nulo");
        
        Preconditions.checkArgument(
            !Strings.isNullOrEmpty(evento.getNombre()), 
            "El nombre del evento no puede estar vacío"
        );
        
        Preconditions.checkNotNull(evento.getSala(), "El evento debe tener una sala asignada");
        
        eventoService.guardarEvento(evento);
        return "redirect:/eventos";
    }

    /**
     * Método que muestra el formulario para editar un evento existente.
     * 
     * @param id El ID del evento que se desea editar.
     * @param modelo El modelo que contiene los atributos necesarios para el formulario de edición.
     * @return La vista del formulario de edición de un evento.
     * @throws IllegalArgumentException Si el ID del evento no es válido.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model modelo) {
        Evento evento = eventoService.obtenerEventoPorId(id);

        //if (evento == null) {
          //  throw new IllegalArgumentException("ID de Evento no válido:" + id);
        //} Reemplazado por preconditions

        Preconditions.checkNotNull(evento, "No se encontró un evento con el ID: %s", id);
        modelo.addAttribute("evento", evento);
        modelo.addAttribute("salas", salaService.listarSalasDisponibles());
        return "eventos/formularioEvento";
    }

    /**
     * Método para eliminar un evento existente.
     * 
     * @param id El ID del evento que se desea eliminar.
     * @return Redirige a la lista de eventos después de eliminar el evento.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return "redirect:/eventos";
    }
    
    /**
     * Maneja la solicitud GET para exportar la lista completa de eventos a un archivo Excel.
     *
     * <p>Este endpoint recupera todos los eventos, utiliza {@code excelService} para
     * generar un archivo Excel (XLSX) en memoria y lo devuelve como un
     * {@link ResponseEntity} que el navegador interpretará como una descarga de archivo.</p>
     *
     * @return Un {@link ResponseEntity} que contiene el archivo Excel como un
     * {@link Resource}. La respuesta incluye las cabeceras HTTP (Content-Disposition)
     * para forzar la descarga con el nombre "reporte_eventos.xlsx".
     */
    @GetMapping("/exportar/excel")
    public ResponseEntity<Resource> exportarEventosExcel() {

        // 1. Obtener los datos
        List<Evento> eventos = eventoService.listarTodosLosEventos();

        // 2. Generar el archivo Excel en memoria
        ByteArrayInputStream excelFile = excelService.crearExcelDeEventos(eventos);

        // 3. Configurar las cabeceras HTTP para la descarga
        HttpHeaders headers = new HttpHeaders();
        String filename = "reporte_eventos.xlsx";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        // 4. Envolver el stream en un recurso de Spring
        InputStreamResource resource = new InputStreamResource(excelFile);

        // 5. Construir y devolver la respuesta HTTP
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}