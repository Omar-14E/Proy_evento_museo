package com.example.museo_v2.controller;

import com.example.museo_v2.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para manejar la página de inicio del museo.
 * Se encarga de cargar los eventos actuales y próximos para mostrarlos en la vista principal.
 */
@Controller
public class HomeController {

    @Autowired
    private EventoService eventoService;

    /**
     * Método que maneja la solicitud de la página de inicio.
     * Carga los eventos del mes actual y los próximos eventos para ser mostrados en la vista.
     * 
     * @param modelo El modelo que contiene los atributos necesarios para la vista.
     * @return La vista principal del museo (index).
     */
    @GetMapping("/")
    public String home(Model modelo) {
        modelo.addAttribute("eventosDelMes", eventoService.obtenerEventosDelMesActual());
        modelo.addAttribute("eventosProximos", eventoService.obtenerProximosEventos(6));
        return "index";
    }

    @GetMapping("/403")
    public String accesoDenegado() {
        return "403"; // Retorna la vista templates/403.html
    }
}
