package com.example.museo_v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad para la aplicación del museo.
 * Define reglas de autorización, autenticación y manejo de excepciones.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean para encriptar contraseñas usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configura la cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configuración de autorización de rutas
            .authorizeHttpRequests(authorize -> authorize
                // Recursos estáticos y páginas públicas (incluyendo la página de error 403)
                .requestMatchers(
                    "/css/**", 
                    "/images/**", 
                    "/usuarios/login", 
                    "/usuarios/registro",
                    "/403"  // ¡Importante! Permitir acceso público a la página de error
                ).permitAll()
                
                // --- REGLAS DE ROLES ---
                
                // Solo el ADMIN puede gestionar usuarios (ver lista, crear admin, etc.)
                .requestMatchers("/usuarios/**").hasRole("ADMIN") 
                
                // Solo el ADMIN puede eliminar registros críticos
                .requestMatchers("/eventos/eliminar/**", "/salas/eliminar/**").hasRole("ADMIN")

                // Para todo lo demás (crear reservas, ver eventos, editar), se requiere estar autenticado
                // (Tanto ADMIN como TRABAJADOR pueden entrar aquí)
                .anyRequest().authenticated()
            )
            
            // 2. Configuración del formulario de inicio de sesión
            .formLogin(form -> form
                .loginPage("/usuarios/login")           // Nuestra vista personalizada
                .loginProcessingUrl("/usuarios/login")  // URL donde se envía el POST del form
                .defaultSuccessUrl("/", true)           // Redirigir al inicio tras login exitoso
                .permitAll()
            )
            
            // 3. Configuración del cierre de sesión
            .logout(logout -> logout
                .logoutSuccessUrl("/usuarios/login?logout")
                .permitAll()
            )

            // 4. Manejo de Excepciones (Página 403 personalizada)
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            );

        return http.build();
    }
}