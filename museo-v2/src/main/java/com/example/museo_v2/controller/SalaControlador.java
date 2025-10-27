package com.example.museo_v2.controller;

import com.example.museo_v2.model.Sala;
import com.example.museo_v2.service.ExcelService;
import com.example.museo_v2.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Controlador para manejar las salas dentro del museo.
 * Permite listar, crear, editar y eliminar salas.
 */
@Controller
@RequestMapping("/salas")
public class SalaControlador {

    @Autowired
    private SalaService salaService;

    @Autowired
    private ExcelService excelService;
    /**
     * Método que lista todas las salas disponibles en el museo.
     * 
     * @param modelo El modelo que contiene los atributos necesarios para la vista.
     * @return La vista de la lista de salas.
     */
    @GetMapping
    public String listarSalas(Model modelo) {
        modelo.addAttribute("salas", salaService.listarTodasLasSalas());
        return "salas/listaSalas";
    }

    /**
     * Método que muestra el formulario para registrar una nueva sala.
     * 
     * @param modelo El modelo que contiene los atributos necesarios para el formulario.
     * @return La vista del formulario de sala.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("sala", new Sala());
        return "salas/formularioSala";
    }

    /**
     * Método para guardar una nueva sala, con la posibilidad de subir una imagen asociada a la sala.
     * 
     * @param sala El objeto Sala que contiene los datos de la sala a guardar.
     * @param file El archivo de imagen asociado a la sala.
     * @return Redirige a la lista de salas después de guardar la nueva sala.
     * @throws IOException Si ocurre un error al procesar el archivo de imagen.
     */
    @PostMapping("/guardar")
    public String guardarSala(@ModelAttribute Sala sala,
            @RequestParam("file") MultipartFile file) throws IOException {

        salaService.guardarSala(sala, file);
        return "redirect:/salas";
    }

    /**
     * Método que muestra el formulario para editar una sala existente.
     * 
     * @param id El ID de la sala que se desea editar.
     * @param modelo El modelo que contiene los atributos necesarios para el formulario de edición.
     * @return La vista del formulario de edición de una sala.
     * @throws IllegalArgumentException Si el ID de la sala no es válido.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model modelo) {
        Sala sala = salaService.obtenerSalaPorId(id);
        if (sala == null) {
            throw new IllegalArgumentException("ID de Sala no válido:" + id);
        }
        modelo.addAttribute("sala", sala);
        return "salas/formularioSala";
    }

    /**
     * Método para eliminar una sala existente.
     * 
     * @param id El ID de la sala que se desea eliminar.
     * @return Redirige a la lista de salas después de eliminar la sala.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarSala(@PathVariable Integer id) {
        salaService.eliminarSala(id);
        return "redirect:/salas";
    }

    @GetMapping("/exportar/excel")
    public ResponseEntity<Resource> exportarSalasExcel() {

        List<Sala> salas = salaService.listarTodasLasSalas();

        ByteArrayInputStream excelFile = excelService.crearExcelDeSalas(salas);

        HttpHeaders headers = new HttpHeaders();
        String filename = "reporte_salas.xlsx";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        InputStreamResource resource = new InputStreamResource(excelFile);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}
