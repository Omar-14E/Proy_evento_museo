package com.example.museo_v2.service;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Implementación del servicio para la gestión de usuarios.
 * Esta clase provee la lógica de negocio sobre la entidad Usuario, utilizando el repositorio.
 *
 * @author TuNombre
 * @since 1.0
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    /**
     * Constructor que inyecta el repositorio de usuarios.
     * @param usuarioRepositorio repositorio JPA para Usuario
     */
    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * @param usuario objeto Usuario a crear
     * @return el Usuario creado
     */
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        String claveEncriptada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(claveEncriptada);
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Obtiene un usuario según su ID.
     * @param id identificador del usuario
     * @return el Usuario encontrado o null si no existe
     */
    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    /**
     * Obtiene la lista de todos los usuarios registrados.
     * @return lista de usuarios
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    /**
     * Actualiza los datos de un usuario existente, identificado por su ID.
     * @param id identificador del usuario a actualizar
     * @param usuario nuevo objeto Usuario con datos actualizados
     * @return el Usuario actualizado
     */
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        usuario.setId(id);
        return usuarioRepositorio.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id identificador del usuario a eliminar
     */
    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param nombreUsuario nombre de usuario del Usuario
     * @return Optional con el Usuario encontrado, o vacío si no existe
     */
    @Override
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
    }
}
