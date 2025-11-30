package com.example.museo_v2.controller;

import com.example.museo_v2.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio del museo.
 * Carga y muestra eventos del mes y próximos eventos.
 */
@Controller
public class HomeController {

    @Autowired
    private EventoService eventoService;

    /**
     * Muestra la página principal con los eventos relevantes.
     *
     * @param modelo modelo para enviar datos a la vista
     * @return nombre de la vista principal
     */
    @GetMapping("/")
    public String home(Model modelo) {
        modelo.addAttribute("eventosDelMes", eventoService.obtenerEventosDelMesActual());
        modelo.addAttribute("eventosProximos", eventoService.obtenerProximosEventos(6));
        return "index";
    }

    /**
     * Muestra la vista de acceso denegado (Error 403).
     *
     * @return nombre de la vista 403
     */
    @GetMapping("/403")
    public String accesoDenegado() {
        return "403";
    }
}
