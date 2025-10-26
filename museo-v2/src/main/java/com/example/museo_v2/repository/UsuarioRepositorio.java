package com.example.museo_v2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.museo_v2.model.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long>{
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
