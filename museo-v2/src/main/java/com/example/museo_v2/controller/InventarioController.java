package com.example.museo_v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.museo_v2.model.ProductoInventario;
import com.example.museo_v2.service.ProductoInventarioService;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private ProductoInventarioService productoInventarioService;

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("productoInventario", new ProductoInventario());
        return "inventario/formularioInventario";
    }

    @PostMapping("/guardar")
    public String guardarProductoInventario(@ModelAttribute ProductoInventario productoInventario) {
        productoInventarioService.guardar(productoInventario);
        return "redirect:/inventario/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("productos", productoInventarioService.findAll());
        return "inventario/listaInventario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        ProductoInventario producto = productoInventarioService.findById(id);
        modelo.addAttribute("productoInventario", producto);
        return "inventario/formularioInventario";
    }

    @PostMapping("/actualizar")
    public String actualizarProductoInventario(@ModelAttribute ProductoInventario productoInventario) {
        productoInventarioService.guardar(productoInventario);
        return "redirect:/inventario/lista";
    }

}
