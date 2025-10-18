package com.example.museo_v2.service;

import com.example.museo_v2.model.Sala;
import com.example.museo_v2.repository.SalaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

/**
 * Implementación del servicio de salas.
 * Proporciona la lógica de negocio para manejar las salas, incluyendo la gestión de imágenes asociadas a ellas.
 */
@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepositorio salaRepositorio;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Obtiene una lista de todas las salas disponibles.
     * 
     * @return Una lista de todas las salas.
     */
    @Override
    public List<Sala> listarTodasLasSalas() {
        return salaRepositorio.findAll();
    }

    /**
     * Obtiene una lista de las salas disponibles.
     * 
     * @return Una lista de salas disponibles.
     */
    @Override
    public List<Sala> listarSalasDisponibles() {
        return salaRepositorio.findAll();
    }

    /**
     * Obtiene una sala por su ID.
     * 
     * @param id El ID de la sala a obtener.
     * @return La sala correspondiente al ID proporcionado o null si no se encuentra.
     */
    @Override
    public Sala obtenerSalaPorId(Integer id) {
        Optional<Sala> sala = salaRepositorio.findById(id);
        return sala.orElse(null);
    }

    /**
     * Guarda una sala en la base de datos, incluyendo la imagen asociada (si se proporciona).
     * 
     * @param sala La sala a guardar.
     * @param file El archivo de la imagen asociada a la sala.
     * @return La sala guardada, con la URL de la imagen si se proporcionó un archivo.
     */
    @Override
    public Sala guardarSala(Sala sala, MultipartFile file) {
        try {
            // Lógica para guardar la imagen asociada a la sala
            if (file != null && !file.isEmpty()) {
                // Obtener el nombre y extensión del archivo
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                
                // Generar un nombre único para el archivo
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                Path fileNameAndPath = Paths.get(uploadDir, uniqueFileName);

                // Crear el directorio si no existe y guardar el archivo
                Files.createDirectories(fileNameAndPath.getParent());
                Files.write(fileNameAndPath, file.getBytes());

                // Establecer la URL de la imagen en la sala
                sala.setImagenUrl("/uploads/salas/" + uniqueFileName);
            } 
            // Si ya existe una sala, pero no se proporciona una nueva imagen
            else if (sala.getId() != null && sala.getImagenUrl() == null) {
                // No se hace nada
            } 
            // Si se está editando una sala, pero no se proporciona una nueva imagen
            else if (sala.getId() != null && (file == null || file.isEmpty())) {
                // Recuperar la imagen existente asociada a la sala
                Sala existingSala = salaRepositorio.findById(sala.getId()).orElse(null);
                if (existingSala != null) {
                    sala.setImagenUrl(existingSala.getImagenUrl());
                }
            }

            // Guardar y devolver la sala
            return salaRepositorio.save(sala);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una sala de la base de datos por su ID, incluyendo la eliminación de la imagen asociada (si existe).
     * 
     * @param id El ID de la sala a eliminar.
     * @throws IllegalArgumentException Si el ID de la sala no es válido.
     */
    @Override
    public void eliminarSala(Integer id) {
        Sala sala = salaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Sala no válido:" + id));

        // Eliminar la imagen asociada a la sala si existe
        if (sala.getImagenUrl() != null && !sala.getImagenUrl().isEmpty()) {
            try {
                String fileName = sala.getImagenUrl().substring(sala.getImagenUrl().lastIndexOf("/") + 1);
                Path imagePath = Paths.get(uploadDir, fileName);

                // Eliminar el archivo de imagen si existe
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Eliminar la sala de la base de datos
        salaRepositorio.delete(sala);
    }
}
