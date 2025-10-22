package com.example.museo_v2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.museo_v2.model.Sala;
import com.example.museo_v2.repository.SalaRepositorio;

@ExtendWith(MockitoExtension.class)
public class SalaServiceImplTest {

    @Mock
    private SalaRepositorio salaRepositorio;

    @InjectMocks
    private SalaServiceImpl salaService;

    private Sala sala;
    private Sala sala2;

    /**
     * Inicializa los objetos necesarios antes de cada prueba.
     * Se asigna un directorio temporal para simular la carga de archivos
     * y se crean dos instancias de {@link Sala} para las pruebas.
     */
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(salaService, "uploadDir", System.getProperty("java.io.tmpdir"));

        sala = new Sala();
        sala.setId(1);
        sala.setNombre("Sala de prueba");
        sala.setCapacidad(60);
        sala.setUbicacion("Pasillo principal");
        sala.setDescripcion("Una sala de prueba ubicada en el pasillo principal");
        sala.setImagenUrl("./src/main/resources/static/uploads/salas/30d37096-2806-436a-ac0e-9ed71a0ac969.jpg");

        sala2 = new Sala();
        sala2.setId(2);
        sala2.setNombre("Sala de prueba numero 2");
        sala2.setCapacidad(40);
        sala2.setUbicacion("Segundo piso");
        sala2.setDescripcion("Una sala de prueba ubicada en el segundo piso");
        sala2.setImagenUrl("./src/main/resources/static/uploads/salas/30d37096-2806-436a-ac0e-9ed71a0ac969.jpg");
    }

    /**
     * Verifica que el método {@code listarTodasLasSalas()} devuelva una lista
     * con todas las salas registradas y que invoque al repositorio una sola vez.
     */
    @Test
    void listarTodasLasSalas_DebeDevolverListaDeSalas() {
        when(salaRepositorio.findAll()).thenReturn(Arrays.asList(sala, sala2));

        List<Sala> salas = salaService.listarTodasLasSalas();

        assertNotNull(salas);
        assertEquals(2, salas.size());
        verify(salaRepositorio, times(1)).findAll();
    }

    /**
     * Verifica que el método {@code obtenerSalaPorId()} devuelva una sala existente
     * cuando el identificador se encuentra en el repositorio.
     */
    @Test
    void obtenerSalaPorId_CuandoExiste_DebeDevolverReserva() {
        when(salaRepositorio.findById(1)).thenReturn(Optional.of(sala));

        Sala salaEncontrada = salaService.obtenerSalaPorId(1);

        assertNotNull(salaEncontrada);
        assertEquals(1, salaEncontrada.getId());
    }

    /**
     * Verifica que el método {@code obtenerSalaPorId()} devuelva {@code null}
     * cuando no existe una sala con el identificador especificado.
     */
    @Test
    void obtenerSalaPorId_CuandoNoExiste_DebeDevolverNull() {
        when(salaRepositorio.findById(99)).thenReturn(Optional.empty());

        Sala salaEncontrada = salaService.obtenerSalaPorId(99);

        assertNull(salaEncontrada);
    }

    /**
     * Verifica que al guardar una sala con una nueva imagen,
     * el servicio asigne correctamente una URL de imagen y guarde la entidad.
     */
    @Test
    void guardarSala_conImagenNueva_debeGuardarSalaConUrl() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg",
                "contenido de prueba".getBytes());

        Sala nuevaSala = new Sala();
        nuevaSala.setNombre("Sala con imagen");

        when(salaRepositorio.save(nuevaSala)).thenReturn(nuevaSala);

        Sala resultado = salaService.guardarSala(nuevaSala, file);

        assertNotNull(resultado);
        assertNotNull(resultado.getImagenUrl());
        assert (resultado.getImagenUrl().contains("/uploads/salas/"));
        verify(salaRepositorio, times(1)).save(nuevaSala);
    }

    /**
     * Verifica que al guardar una sala sin imagen nueva,
     * se guarde sin modificar el campo {@code imagenUrl}.
     */
    @Test
    void guardarSala_sinImagenNueva_debeGuardarSalaSinCambiosEnUrl() {
        Sala nuevaSala = new Sala();
        nuevaSala.setNombre("Sala sin imagen");

        when(salaRepositorio.save(nuevaSala)).thenReturn(nuevaSala);

        Sala resultado = salaService.guardarSala(nuevaSala, null);

        assertNotNull(resultado);
        assertNull(resultado.getImagenUrl());
        verify(salaRepositorio, times(1)).save(nuevaSala);
    }

    /**
     * Verifica que si se guarda una sala existente sin nueva imagen,
     * se conserve la URL anterior en lugar de sobrescribirla.
     */
    @Test
    void guardarSala_existenteSinNuevaImagen_debeMantenerImagenAnterior() {
        Sala existente = new Sala();
        existente.setId(1);
        existente.setImagenUrl("/uploads/salas/imagen-vieja.jpg");

        when(salaRepositorio.findById(1)).thenReturn(Optional.of(existente));
        when(salaRepositorio.save(existente)).thenReturn(existente);

        Sala resultado = salaService.guardarSala(existente, null);

        assertNotNull(resultado);
        assertEquals("/uploads/salas/imagen-vieja.jpg", resultado.getImagenUrl());
        verify(salaRepositorio, times(1)).findById(1);
        verify(salaRepositorio, times(1)).save(existente);
    }

    /**
     * Verifica que, si ocurre una {@link IOException} durante la escritura del
     * archivo,
     * el método {@code guardarSala()} devuelva {@code null} y no lance una
     * excepción.
     */
    @Test
    void guardarSala_cuandoOcurreIOException_debeDevolverNull() throws Exception {
        Sala sala = new Sala();
        MockMultipartFile file = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "bytes".getBytes());

        try (MockedStatic<Files> mockedFiles = org.mockito.Mockito.mockStatic(Files.class)) {
            mockedFiles
                    .when(() -> Files.write(ArgumentMatchers.<Path>any(), ArgumentMatchers.<byte[]>any()))
                    .thenThrow(new IOException("Error simulado"));

            Sala resultado = salaService.guardarSala(sala, file);
            assertNull(resultado);
        }
    }
}
