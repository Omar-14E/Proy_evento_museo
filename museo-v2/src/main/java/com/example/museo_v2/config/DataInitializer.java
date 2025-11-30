package com.example.museo_v2.config;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inicializa datos esenciales al arrancar la aplicación,
 * creando el usuario administrador por defecto si no existe.
 */
@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crea un {@link CommandLineRunner} que registra un usuario administrador
     * inicial en caso de no existir.
     *
     * @return instancia de CommandLineRunner para inicializar datos
     */
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (usuarioRepositorio.findByNombreUsuario("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombres("Administrador Principal");
                admin.setNombreUsuario("admin");
                admin.setClave(passwordEncoder.encode("admin123"));
                admin.setRol("ROLE_ADMIN");

                usuarioRepositorio.save(admin);
                System.out.println(">>> USUARIO ADMIN CREADO: admin / admin123");
            }
        };
    }
}
