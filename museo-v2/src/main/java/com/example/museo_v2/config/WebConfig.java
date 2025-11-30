package com.example.museo_v2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configura el manejo de recursos estáticos, 
 * permitiendo acceder a archivos subidos desde una ruta externa.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Registra un manejador de recursos para servir imágenes de salas
     * desde el directorio configurado en el sistema.
     *
     * @param registry registro de manejadores de recursos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file:" + uploadDir;
        
        if (!path.endsWith("/")) {
            path += "/";
        }

        registry.addResourceHandler("/uploads/salas/**")
                .addResourceLocations(path);
    }
}
