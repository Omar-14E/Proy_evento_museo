package com.example.museo_v2.service;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.repository.EventoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceImplTest {

    @Mock 
    private EventoRepositorio eventoRepositorio;

    @InjectMocks 
    private EventoServiceImpl eventoService;

    private Evento evento1;
    private Evento evento2;

    @BeforeEach
    void setUp() {
        evento1 = new Evento();
        evento1.setId(1L); 
        evento1.setNombre("Evento Pasado");
        evento1.setFechaInicio(LocalDate.now().minusDays(10));

        evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNombre("Evento Futuro");
        evento2.setFechaInicio(LocalDate.now().plusDays(10));
    }

    @Test
    void listarTodosLosEventos_DebeDevolverListaDeEventos() {
        when(eventoRepositorio.findAll()).thenReturn(Arrays.asList(evento1, evento2));

        List<Evento> eventos = eventoService.listarTodosLosEventos();

        assertNotNull(eventos);
        assertEquals(2, eventos.size());
        verify(eventoRepositorio, times(1)).findAll(); 
    }

    @Test
    void obtenerEventoPorId_CuandoExiste_DebeDevolverEvento() {
        when(eventoRepositorio.findById(1L)).thenReturn(Optional.of(evento1));

        Evento eventoEncontrado = eventoService.obtenerEventoPorId(1L);

        assertNotNull(eventoEncontrado);
        assertEquals(1L, eventoEncontrado.getId());
    }

    @Test
    void obtenerEventoPorId_CuandoNoExiste_DebeDevolverNull() {
        when(eventoRepositorio.findById(99L)).thenReturn(Optional.empty());

        Evento eventoEncontrado = eventoService.obtenerEventoPorId(99L);

        assertNull(eventoEncontrado);
    }

    @Test
    void guardarEvento_DebeLlamarAlRepositorioSave() {
        when(eventoRepositorio.save(any(Evento.class))).thenReturn(evento1);

        Evento eventoGuardado = eventoService.guardarEvento(evento1);

        assertNotNull(eventoGuardado);
        verify(eventoRepositorio, times(1)).save(evento1);
    }
    
    @Test
    void eliminarEvento_DebeLlamarAlRepositorioDelete() {
        Long eventoId = 1L;
        doNothing().when(eventoRepositorio).deleteById(eventoId);

        eventoService.eliminarEvento(eventoId);

        verify(eventoRepositorio, times(1)).deleteById(eventoId);
    }

    @Test
    void obtenerProximosEventos_CuandoHayMasDelLimite_DebeDevolverListaLimitada() {
        List<Evento> muchosEventosFuturos = Arrays.asList(new Evento(), new Evento(), new Evento(), new Evento(), new Evento(), new Evento());
        when(eventoRepositorio.findByFechaInicioAfterOrderByFechaInicioAsc(any(LocalDate.class)))
                .thenReturn(muchosEventosFuturos);
        
        int limit = 5;

        List<Evento> proximosEventos = eventoService.obtenerProximosEventos(limit);

        assertNotNull(proximosEventos);
        assertEquals(limit, proximosEventos.size()); 
    }
    
    @Test
    void obtenerProximosEventos_CuandoHayMenosDelLimite_DebeDevolverListaCompleta() {
        List<Evento> pocosEventosFuturos = Arrays.asList(evento2);
        when(eventoRepositorio.findByFechaInicioAfterOrderByFechaInicioAsc(any(LocalDate.class)))
                .thenReturn(pocosEventosFuturos);
        
        int limit = 5;

        List<Evento> proximosEventos = eventoService.obtenerProximosEventos(limit);

        assertNotNull(proximosEventos);
        assertEquals(1, proximosEventos.size());
    }
}