package com.example.museo_v2.service;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

/**
 * Implementación del servicio para gestionar usuarios.
 * Proporciona operaciones CRUD y soporte para autenticación.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que inyecta el repositorio y el codificador de contraseñas.
     */
    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo usuario con contraseña encriptada.
     */
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        String claveEncriptada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(claveEncriptada);
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     */
    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    /**
     * Devuelve la lista completa de usuarios.
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        usuario.setId(id);
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     */
    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     */
    @Override
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
    }

    /**
     * Carga los detalles del usuario para la autenticación de Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(username)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("Usuario no encontrado: " + username));

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol());

        return new User(
            usuario.getNombreUsuario(), 
            usuario.getClave(), 
            Collections.singletonList(authority) // Pasamos la autoridad aquí
        );
    }
}
