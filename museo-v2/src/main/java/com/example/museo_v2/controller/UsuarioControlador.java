package com.example.museo_v2.controller;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Controlador para la gestión de usuarios: login, registro y listado.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioService usuarioService;

    public UsuarioControlador(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra la vista de inicio de sesión.
     *
     * @return nombre de la vista de login
     */
    @GetMapping("/login")
    public String login() {
        return "usuarios/login";
    }

    /**
     * Muestra el formulario de registro.
     *
     * @param model modelo para transportar el objeto usuario
     * @return vista de registro
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/registro";
    }

    /**
     * Procesa el registro de un nuevo usuario.
     *
     * @param usuario datos del formulario
     * @param result validación del formulario
     * @param model modelo para la vista
     * @return redirección a login si es exitoso, o vista de registro si falla
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "usuarios/registro";
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ROLE_TRABAJADOR");
        }

        usuarioService.crearUsuario(usuario);
        return "redirect:/usuarios/login";
    }

    /**
     * Muestra la lista de usuarios registrados.
     *
     * @param model modelo para enviar la lista de usuarios
     * @return vista del listado de usuarios
     */
    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/usuarios";
    }
}
