package com.example.museo_v2.controller;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.service.EventoService;
import com.example.museo_v2.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return "redirect:/eventos";
    }
}
