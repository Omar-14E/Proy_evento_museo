package com.example.museo_v2.controller;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import java.util.List;

/**
 * Controlador para la gestión de usuarios y manejo de vistas Thymeleaf (login,
 * registro, listado).
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioService usuarioService;
    
    public UsuarioControlador(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Vista de login.
     * 
     * @return nombre archivo Thymeleaf login.html
     */
    @GetMapping("/login")
    public String login() {
        return "usuarios/login";
    }

    /**
     * Vista de registro de usuario.
     * 
     * @param model modelo para el formulario
     * @return nombre archivo Thymeleaf registro.html
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/registro";
    }

    /**
     * Procesa el registro del usuario desde el formulario.
     * 
     * @param usuario datos desde el formulario
     * @param result  validación
     * @param model   modelo para la vista
     * @return a login si ok, o vuelve a registro si falla
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "usuarios/registro";
        }
        
        // Asignar rol por defecto si no viene del formulario
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ROLE_TRABAJADOR");
        }
        
        usuarioService.crearUsuario(usuario);
        return "redirect:/usuarios/login";
    }

    /**
     * Vista de listado de usuarios
     * 
     * @param model Modelo para pasar usuarios
     * @return nombre archivo Thymeleaf usuarios.html
     */
    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/usuarios";
    }
}
