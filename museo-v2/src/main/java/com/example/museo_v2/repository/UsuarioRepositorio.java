package com.example.museo_v2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.museo_v2.model.Usuario;

/**
 * Repositorio para la gestión de usuarios.
 * Permite realizar operaciones CRUD y búsquedas específicas.
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario a buscar
     * @return un {@link Optional} que contiene el usuario si existe
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
