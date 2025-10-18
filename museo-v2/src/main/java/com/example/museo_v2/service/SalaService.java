package com.example.museo_v2.service;

import com.example.museo_v2.model.Sala;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Interfaz de servicio para manejar las operaciones relacionadas con las salas.
 * Proporciona métodos para listar, guardar, obtener, eliminar y listar las salas disponibles.
 */
public interface SalaService {
    
    /**
     * Obtiene una lista de todas las salas.
     * 
     * @return Una lista de todas las salas.
     */
    List<Sala> listarTodasLasSalas();
    
    /**
     * Guarda una sala en la base de datos, incluyendo la imagen asociada (si se proporciona).
     * 
     * @param sala La sala a guardar.
     * @param file El archivo de la imagen asociada a la sala.
     * @return La sala guardada.
     */
    Sala guardarSala(Sala sala, MultipartFile file);
    
    /**
     * Obtiene una sala por su ID.
     * 
     * @param id El ID de la sala a obtener.
     * @return La sala correspondiente al ID proporcionado o null si no se encuentra.
     */
    Sala obtenerSalaPorId(Integer id);
    
    /**
     * Elimina una sala de la base de datos por su ID.
     * 
     * @param id El ID de la sala a eliminar.
     */
    void eliminarSala(Integer id);
    
    /**
     * Obtiene una lista de las salas disponibles.
     * 
     * @return Una lista de salas disponibles.
     */
    List<Sala> listarSalasDisponibles(); 
}
