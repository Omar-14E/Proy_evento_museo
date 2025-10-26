package com.example.museo_v2.service;

import com.example.museo_v2.model.Usuario;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de usuarios.
 */
@Service
public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);

    Usuario obtenerUsuarioPorId(Long id);

    List<Usuario> listarUsuarios();

    Usuario actualizarUsuario(Long id, Usuario usuario);

    void eliminarUsuario(Long id);

    Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario);
}
