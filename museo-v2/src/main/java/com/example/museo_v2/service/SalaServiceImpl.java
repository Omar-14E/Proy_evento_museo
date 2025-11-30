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
 * Servicio que gestiona las salas del museo, incluyendo el manejo de imágenes
 * asociadas para su almacenamiento y actualización.
 */
@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepositorio salaRepositorio;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Obtiene todas las salas registradas.
     *
     * @return lista completa de salas
     */
    @Override
    public List<Sala> listarTodasLasSalas() {
        return salaRepositorio.findAll();
    }

    /**
     * Obtiene las salas disponibles.
     * Actualmente retorna todas, pero se deja para futura lógica de disponibilidad.
     *
     * @return lista de salas disponibles
     */
    @Override
    public List<Sala> listarSalasDisponibles() {
        return salaRepositorio.findAll();
    }

    /**
     * Busca una sala por su identificador.
     *
     * @param id identificador de la sala
     * @return la sala encontrada o {@code null} si no existe
     */
    @Override
    public Sala obtenerSalaPorId(Integer id) {
        Optional<Sala> sala = salaRepositorio.findById(id);
        return sala.orElse(null);
    }

    /**
     * Guarda una sala en la base de datos con o sin actualización de imagen.
     * Si se proporciona un archivo válido, se genera un nombre único y se almacena físicamente.
     * Si la sala ya existe y no se sube imagen nueva, se conserva la imagen previa.
     *
     * @param sala sala a guardar
     * @param file archivo de la imagen asociada
     * @return sala guardada
     */
    @Override
    public Sala guardarSala(Sala sala, MultipartFile file) {
        try {
            if (sala.getId() != null) {
                Sala salaExistente = salaRepositorio.findById(sala.getId()).orElse(null);
                if (salaExistente != null) {
                    sala.setImagenUrl(salaExistente.getImagenUrl());
                }
            }

            boolean hayArchivoNuevo =
                    file != null && !StringUtils.isBlank(file.getOriginalFilename());

            if (hayArchivoNuevo) {
                String originalFileName = file.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(originalFileName);

                String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                Path fileNameAndPath = Paths.get(uploadDir, uniqueFileName);

                Files.createDirectories(fileNameAndPath.getParent());
                Files.write(fileNameAndPath, file.getBytes());

                sala.setImagenUrl("/uploads/salas/" + uniqueFileName);
            }

            return salaRepositorio.save(sala);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una sala por su identificador, incluyendo la imagen asociada si existe.
     *
     * @param id identificador de la sala
     * @throws IllegalArgumentException si la sala no existe
     */
    @Override
    public void eliminarSala(Integer id) {
        Sala sala = salaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Sala no válido: " + id));

        if (sala.getImagenUrl() != null && !sala.getImagenUrl().isEmpty()) {
            try {
                String fileName =
                        sala.getImagenUrl().substring(sala.getImagenUrl().lastIndexOf("/") + 1);
                Path imagePath = Paths.get(uploadDir, fileName);

                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        salaRepositorio.delete(sala);
    }
}
