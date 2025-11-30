package com.example.museo_v2.controller;

import com.example.museo_v2.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la visualización del panel de control.
 * Gestiona la carga de métricas generales del sistema.
 */
@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Muestra el panel de control cargando todas las métricas disponibles.
     *
     * @param model modelo utilizado para enviar datos a la vista
     * @return nombre de la vista del dashboard
     */
    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        model.addAllAttributes(dashboardService.obtenerMetricas());
        return "dashboard";
    }
}
