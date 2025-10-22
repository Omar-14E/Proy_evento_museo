package com.example.museo_v2.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

import com.example.museo_v2.model.Evento;
import com.example.museo_v2.model.Reserva;
import com.example.museo_v2.repository.ReservaRepositorio;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepositorio reservaRepositorio;

    @Mock
    private EventoService eventoService;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva1;
    private Evento evento1;

    /**
     * Inicializa los objetos de prueba antes de cada test.
     * Se configura un evento y una reserva de ejemplo.
     */
    @BeforeEach
    void setUp() {
        evento1 = new Evento();
        evento1.setId(1L);
        evento1.setNombre("Evento Pasado");
        evento1.setFechaInicio(LocalDate.now().minusDays(10));

        reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setNombreCompleto("Reserva numero 1");
        reserva1.setDniRuc("12345678");
        reserva1.setTelefono("987654321");
        reserva1.setNEntradas(10);
        reserva1.setTotalPagar(BigDecimal.valueOf(50.5));
        reserva1.setEvento(evento1);
        reserva1.setTipoComprobante("Boleta");
        reserva1.setMetodoPago("Efectivo");
        reserva1.setFechaReserva(LocalDateTime.now());
    }

    /**
     * Verifica que al guardar una reserva, se invoque correctamente
     * el método {@code save()} del repositorio y {@code obtenerEventoPorId()}
     * del servicio de eventos.
     */
    @Test
    void guardarReserva_DebeLlamarAlRepositorioSave() {
        when(eventoService.obtenerEventoPorId(evento1.getId())).thenReturn(evento1);
        when(reservaRepositorio.save(any(Reserva.class))).thenReturn(reserva1);

        Reserva reservaGuardada = reservaService.guardarReserva(reserva1, evento1.getId());

        assertNotNull(reservaGuardada);
        verify(reservaRepositorio, times(1)).save(reserva1);
        verify(eventoService, times(1)).obtenerEventoPorId(evento1.getId());
    }

    /**
     * Verifica que se devuelva una reserva existente al buscar por su ID.
     */
    @Test
    void obtenerReservaPorId_CuandoExiste_DebeDevolverReserva() {
        when(reservaRepositorio.findById(1L)).thenReturn(Optional.of(reserva1));

        Reserva reservaEncontrada = reservaService.obtenerReservaPorId(1L);

        assertNotNull(reservaEncontrada);
        assertEquals(1L, reservaEncontrada.getId());
    }

    /**
     * Verifica que se devuelva {@code null} al buscar una reserva inexistente.
     */
    @Test
    void obtenerReservaPorId_CuandoNoExiste_DebeDevolverNull() {
        when(reservaRepositorio.findById(99L)).thenReturn(Optional.empty());

        Reserva reservaEncontrada = reservaService.obtenerReservaPorId(99L);

        assertNull(reservaEncontrada);
    }
}
