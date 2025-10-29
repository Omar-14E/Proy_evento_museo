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
 * 
 * <p>Esta clase define la configuración de autenticación y autorización 
 * utilizando Spring Security. Se encarga de proteger las rutas de la aplicación,
 * establecer la página de inicio de sesión y definir el comportamiento 
 * del cierre de sesión.</p>
 * 
 * <p>La anotación {@link Configuration} indica que esta clase 
 * contiene beans de configuración. La anotación {@link EnableWebSecurity}
 * habilita las características de seguridad web de Spring Security.</p>
 * 
 * @author Omar
 * @version 2.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean que define el codificador de contraseñas.
     * 
     * <p>Se utiliza {@link BCryptPasswordEncoder} para encriptar las contraseñas 
     * de los usuarios antes de guardarlas en la base de datos. 
     * BCrypt aplica un algoritmo de hash seguro que incluye un salt aleatorio 
     * para evitar ataques por diccionario.</p>
     * 
     * @return un objeto {@link PasswordEncoder} basado en {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configura las reglas de seguridad HTTP para la aplicación.
     * 
     * <p>Define qué rutas pueden ser accedidas sin autenticación y cuáles requieren 
     * que el usuario haya iniciado sesión. También establece la página de inicio de sesión 
     * personalizada y el comportamiento al cerrar sesión.</p>
     * 
     * @param http objeto {@link HttpSecurity} que permite configurar la seguridad HTTP.
     * @return una instancia de {@link SecurityFilterChain} con las reglas configuradas.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización de rutas
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas que no requieren autenticación
                .requestMatchers(
                    "/usuarios/login",     // Página de inicio de sesión
                    "/usuarios/registro",  // Página de registro de nuevos usuarios
                    "/css/**",             // Recursos estáticos (estilos CSS)
                    "/images/**"           // Imágenes accesibles públicamente
                ).permitAll()
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración del formulario de inicio de sesión
            .formLogin(form -> form
                .loginPage("/usuarios/login")           // URL de la página de login personalizada
                .loginProcessingUrl("/usuarios/login")  // URL que procesa el formulario de login
                .defaultSuccessUrl("/", true)           // Redirección tras inicio de sesión exitoso
                .permitAll()
            )
            
            // Configuración del cierre de sesión
            .logout(logout -> logout
                .logoutSuccessUrl("/usuarios/login?logout") // Redirección tras cerrar sesión
                .permitAll()
            );

        // Construye y devuelve la cadena de filtros de seguridad
        return http.build();
    }
}
