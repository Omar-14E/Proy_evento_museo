package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.repository.EventoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de eventos.
 * Proporciona la lógica de negocio para manejar los eventos y delega las operaciones CRUD en el repositorio.
 */
@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepositorio eventoRepositorio;

    /**
     * Obtiene una lista de todos los eventos.
     * 
     * @return Una lista de todos los eventos.
     */
    @Override
    public List<Evento> listarTodosLosEventos() {
        return eventoRepositorio.findAll();
    }

    /**
     * Guarda un nuevo evento o actualiza uno existente.
     * 
     * @param evento El evento a guardar o actualizar.
     * @return El evento guardado o actualizado.
     */
    @Override
    public Evento guardarEvento(Evento evento) {
        return eventoRepositorio.save(evento);
    }

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id El ID del evento a obtener.
     * @return El evento correspondiente al ID proporcionado o null si no se encuentra.
     */
    @Override
    public Evento obtenerEventoPorId(Long id) {
        Optional<Evento> evento = eventoRepositorio.findById(id);
        return evento.orElse(null);
    }

    /**
     * Elimina un evento de la base de datos por su ID.
     * 
     * @param id El ID del evento a eliminar.
     */
    @Override
    public void eliminarEvento(Long id) {
        eventoRepositorio.deleteById(id);
    }

    /**
     * Obtiene los eventos que ocurren en el mes actual.
     * 
     * @return Una lista de eventos que ocurren en el mes actual.
     */
    @Override
    public List<Evento> obtenerEventosDelMesActual() {
        LocalDate hoy = LocalDate.now();
        return eventoRepositorio.findEventosByMonthAndYear(hoy);
    }

    /**
     * Obtiene una lista de los próximos eventos, limitado a un número específico.
     * 
     * @param limit El número máximo de eventos a devolver.
     * @return Una lista de los próximos eventos, limitada por el parámetro `limit`.
     */
    @Override
    public List<Evento> obtenerProximosEventos(int limit) {
        LocalDate hoy = LocalDate.now();
        List<Evento> proximos = eventoRepositorio.findByFechaInicioAfterOrderByFechaInicioAsc(hoy);

        // Limita la cantidad de eventos devueltos si excede el límite especificado.
        if (proximos.size() > limit) {
            return proximos.subList(0, limit);
        }
        return proximos;
    }
}
