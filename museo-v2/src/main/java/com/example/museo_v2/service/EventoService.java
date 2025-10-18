package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import java.util.List;

/**
 * Interfaz de servicio para manejar las operaciones relacionadas con los eventos.
 * Proporciona métodos para listar, guardar, obtener, eliminar y obtener eventos del mes actual y futuros.
 */
public interface EventoService {
    
    /**
     * Obtiene una lista de todos los eventos.
     * 
     * @return Una lista de todos los eventos.
     */
    List<Evento> listarTodosLosEventos();
    
    /**
     * Guarda un evento en la base de datos.
     * 
     * @param evento El evento a guardar.
     * @return El evento guardado.
     */
    Evento guardarEvento(Evento evento);
    
    /**
     * Obtiene un evento por su ID.
     * 
     * @param id El ID del evento a obtener.
     * @return El evento correspondiente al ID proporcionado.
     */
    Evento obtenerEventoPorId(Long id);
    
    /**
     * Elimina un evento de la base de datos por su ID.
     * 
     * @param id El ID del evento a eliminar.
     */
    void eliminarEvento(Long id);
    
    /**
     * Obtiene los eventos que ocurren en el mes actual.
     * 
     * @return Una lista de eventos del mes actual.
     */
    List<Evento> obtenerEventosDelMesActual();
    
    /**
     * Obtiene una lista de los próximos eventos, limitado a un número especificado.
     * 
     * @param limit El número máximo de eventos a devolver.
     * @return Una lista de los próximos eventos.
     */
    List<Evento> obtenerProximosEventos(int limit);
}
