package com.example.museo_v2.controller;

import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.service.ExcelService;
import com.example.museo_v2.service.ProductoInventarioService;
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
import java.util.List;

/**
 * Controlador para la gestión del inventario del museo.
 * Permite registrar, editar, listar y exportar productos del inventario.
 */
@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private ProductoInventarioService productoInventarioService;

    @Autowired
    private ExcelService excelService;

    /**
     * Lista todos los productos del inventario.
     *
     * @param model modelo con la lista de productos
     * @return vista de lista de inventario
     */
    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("productos", productoInventarioService.findAll());
        return "inventario/listaInventario";
    }

    /**
     * Muestra el formulario para registrar un nuevo producto.
     *
     * @param modelo modelo con un objeto vacío
     * @return vista del formulario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("productoInventario", new ProductoInventario());
        return "inventario/formularioInventario";
    }

    /**
     * Guarda un nuevo producto en el inventario.
     *
     * @param productoInventario producto a guardar
     * @return redirección a la lista de inventario
     */
    @PostMapping("/guardar")
    public String guardarProductoInventario(@ModelAttribute ProductoInventario productoInventario) {
        productoInventarioService.guardar(productoInventario);
        return "redirect:/inventario/lista";
    }

    /**
     * Muestra el formulario para editar un producto existente.
     *
     * @param id     ID del producto
     * @param modelo modelo con los datos del producto
     * @return vista del formulario
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        ProductoInventario producto = productoInventarioService.findById(id);
        modelo.addAttribute("productoInventario", producto);
        return "inventario/formularioInventario";
    }

    /**
     * Actualiza un producto existente del inventario.
     *
     * @param productoInventario datos actualizados
     * @return redirección a la lista de inventario
     */
    @PostMapping("/actualizar")
    public String actualizarProductoInventario(@ModelAttribute ProductoInventario productoInventario) {
        productoInventarioService.guardar(productoInventario);
        return "redirect:/inventario/lista";
    }

    /**
     * Exporta los productos del inventario a un archivo Excel.
     *
     * @return archivo Excel como recurso descargable
     */
    @GetMapping("/exportar/excel")
    public ResponseEntity<Resource> exportarInventarioExcel() {
        List<ProductoInventario> productos = productoInventarioService.findAll();
        ByteArrayInputStream excelFile = excelService.crearExcelDeInventario(productos);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_inventario.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}
