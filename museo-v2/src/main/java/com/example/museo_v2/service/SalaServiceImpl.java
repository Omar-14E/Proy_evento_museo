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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

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
     * Este método ha sido refactorizado para usar Apache Commons IO y Lang.
     *
     * @param sala La sala a guardar.
     * @param file El archivo de la imagen asociada a la sala.
     * @return La sala guardada.
     */
    @Override
    public Sala guardarSala(Sala sala, MultipartFile file) {
        try {
            // 1. LÓGICA DE ACTUALIZACIÓN (para no perder la imagen si no se cambia)
            // Si la sala ya tiene un ID, significa que la estamos editando.
            if (sala.getId() != null) {
                // Obtenemos la URL de la imagen que ya está en la BD.
                Sala salaExistente = salaRepositorio.findById(sala.getId())
                        .orElse(null);

                if (salaExistente != null) {
                    // Establecemos la imagenUrl antigua por defecto.
                    // Si se sube una nueva imagen, esta línea se sobrescribirá más abajo.
                    sala.setImagenUrl(salaExistente.getImagenUrl());
                }
            }

            // 2. --- AQUÍ USAMOS COMMONS LANG ---
            // Verificamos si se subió un archivo válido.
            // StringUtils.isBlank() comprueba que el nombre no sea null, ni "", ni " " (solo espacios).

            boolean hayArchivoNuevo = (file != null && !StringUtils.isBlank(file.getOriginalFilename()));

            if (hayArchivoNuevo) {

                String originalFileName = file.getOriginalFilename();

                // 3. --- AQUÍ USAMOS COMMONS IO ---
                // Obtenemos la extensión del archivo de forma segura (ej: "jpg", "png").
                String fileExtension = FilenameUtils.getExtension(originalFileName);

                // (Si quieres borrar la imagen antigua antes de guardar la nueva, este es un buen lugar)

                // 4. CREACIÓN DE NOMBRE Y GUARDADO
                String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                Path fileNameAndPath = Paths.get(uploadDir, uniqueFileName);

                Files.createDirectories(fileNameAndPath.getParent());
                Files.write(fileNameAndPath, file.getBytes());

                // 5. Actualizamos la URL en el objeto 'sala'
                sala.setImagenUrl("/uploads/salas/" + uniqueFileName);
            }

            // 6. GUARDADO FINAL
            // Guardamos la sala (ya sea con la URL de la imagen nueva o con la antigua)
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
