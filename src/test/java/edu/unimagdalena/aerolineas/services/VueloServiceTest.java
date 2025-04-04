package edu.unimagdalena.aerolineas.services;

import edu.unimagdalena.aerolineas.entities.Vuelo;
import edu.unimagdalena.aerolineas.exceptions.VueloNotFoundException;
import edu.unimagdalena.aerolineas.repositories.RepositoryVuelo;
import edu.unimagdalena.aerolineas.services.impl.VueloServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VueloServiceTest {

    @Mock
    private RepositoryVuelo vueloRepository;

    @InjectMocks
    private VueloServiceImpl vueloService;

    public VueloServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearVuelo() {
        Vuelo vuelo = new Vuelo(null, UUID.randomUUID(), "BOG", "MDE", null, null);
        Vuelo savedVuelo = new Vuelo(1L, vuelo.getNumero(), vuelo.getOrigen(), vuelo.getDestino(), null, null);

        when(vueloRepository.save(any(Vuelo.class))).thenReturn(savedVuelo);

        Vuelo result = vueloService.crearVuelo(vuelo);

        assertNotNull(result.getIdVuelo());
        assertEquals("BOG", result.getOrigen());
        verify(vueloRepository, times(1)).save(any(Vuelo.class));
    }

    @Test
    void buscarPorOrigenYDestino() {
        Vuelo vuelo = new Vuelo(1L, UUID.randomUUID(), "BOG", "MDE", null, null);
        when(vueloRepository.findByOrigenAndDestino("BOG", "MDE")).thenReturn(List.of(vuelo));

        List<Vuelo> resultados = vueloService.buscarPorOrigenYDestino("BOG", "MDE");

        assertEquals(1, resultados.size());
        assertEquals("MDE", resultados.get(0).getDestino());
        verify(vueloRepository, times(1)).findByOrigenAndDestino("BOG", "MDE");
    }

    @Test
    void encontrarVueloConDestinoMasLargo() {
        Vuelo vuelo = new Vuelo(1L, UUID.randomUUID(), "BOG", "Ciudad de Mexico", null, null);
        when(vueloRepository.encontrarVueloConDestinoMasLargo()).thenReturn(vuelo);

        Vuelo resultado = vueloService.encontrarVueloConDestinoMasLargo();

        assertEquals("Ciudad de Mexico", resultado.getDestino());
        verify(vueloRepository, times(1)).encontrarVueloConDestinoMasLargo();
    }

    @Test
    void actualizarVuelo_PasajeroExiste() {
        Long id = 1L;
        Vuelo existente = new Vuelo(id, UUID.randomUUID(), "BOG", "MDE", null, null);
        Vuelo actualizado = new Vuelo(id, UUID.randomUUID(), "CTG", "MDE", null, null);

        when(vueloRepository.findById(id)).thenReturn(Optional.of(existente));
        when(vueloRepository.save(existente)).thenReturn(actualizado);

        Vuelo resultado = vueloService.actualizarVuelo(id, actualizado);

        assertEquals("CTG", resultado.getOrigen());
        verify(vueloRepository, times(1)).findById(id);
        verify(vueloRepository, times(1)).save(existente);
    }

    @Test
    void actualizarVuelo_VueloNoExiste() {
        Long id = 99L;
        Vuelo actualizado = new Vuelo(id, UUID.randomUUID(), "BOG", "MDE", null, null);

        when(vueloRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(VueloNotFoundException.class, () -> {
            vueloService.actualizarVuelo(id, actualizado);
        });
    }

    @Test
    void eliminarVuelo_VueloExiste() {
        Long id = 1L;
        when(vueloRepository.existsById(id)).thenReturn(true);

        vueloService.eliminarVuelo(id);

        verify(vueloRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarVuelo_VueloNoExiste() {
        Long id = 99L;
        when(vueloRepository.existsById(id)).thenReturn(false);

        assertThrows(VueloNotFoundException.class, () -> {
            vueloService.eliminarVuelo(id);
        });
    }

    @Test
    void buscarVuelosNumeroTerminaEn() {
        UUID numero = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Vuelo vuelo = new Vuelo(1L, numero, "BOG", "MDE", null, null);
        when(vueloRepository.encontrarVuelosPorNumeroTerminandoEn("000")).thenReturn(List.of(vuelo));

        List<Vuelo> resultados = vueloService.buscarVuelosNumeroTerminaEn("000");

        assertFalse(resultados.isEmpty());
        assertEquals(numero, resultados.get(0).getNumero());
    }

    @Test
    void contarVuelosPorOrigen() {
        when(vueloRepository.contarPorOrigen("BOG")).thenReturn(5L);

        long conteo = vueloService.contarVuelosPorOrigen("BOG");

        assertEquals(5, conteo);
        verify(vueloRepository, times(1)).contarPorOrigen("BOG");
    }

    @Test
    void buscarPorFragmentoNumero() {
        UUID numero = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Vuelo vuelo = new Vuelo(1L, numero, "BOG", "MDE", null, null);
        when(vueloRepository.encontrarPorNumeroQueContenga("456")).thenReturn(List.of(vuelo));

        List<Vuelo> resultados = vueloService.buscarPorFragmentoNumero("456");

        assertEquals(1, resultados.size());
        assertTrue(resultados.get(0).getNumero().toString().contains("456"));
    }
}