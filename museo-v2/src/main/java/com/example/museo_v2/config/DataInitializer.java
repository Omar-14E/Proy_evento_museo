package com.example.museo_v2.config;

import com.example.museo_v2.model.Usuario;
import com.example.museo_v2.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Verificamos si existe el usuario admin, si no, lo creamos
            if (usuarioRepositorio.findByNombreUsuario("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombres("Administrador Principal");
                admin.setNombreUsuario("admin");
                admin.setClave(passwordEncoder.encode("admin123")); // Contraseña inicial
                admin.setRol("ROLE_ADMIN"); // Rol Maestro
                
                usuarioRepositorio.save(admin);
                System.out.println(">>> USUARIO ADMIN CREADO: admin / admin123");
            }
        };
    }
}