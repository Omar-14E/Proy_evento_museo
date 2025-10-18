package com.example.museo_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot.
 * Esta clase arranca la aplicación y configura el contexto de Spring.
 */
@SpringBootApplication
public class MuseoV2Application {

    /**
     * Método principal que arranca la aplicación Spring Boot.
     * 
     * @param args Argumentos de línea de comandos, que no se utilizan en este caso.
     */
    public static void main(String[] args) {
        // Inicia la aplicación Spring Boot
        SpringApplication.run(MuseoV2Application.class, args);
    }
}
