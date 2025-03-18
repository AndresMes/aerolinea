package edu.unimagdalena.aerolineas.controllers;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.entities.Reserva;
import edu.unimagdalena.aerolineas.exceptions.PasajeroNotFoundException;
import edu.unimagdalena.aerolineas.services.PasajeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PasajeroControllerTest {

    @Mock
    private PasajeroService pasajeroService;

    @InjectMocks
    private PasajeroController pasajeroController;

    private Pasajero pasajeroConReserva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar datos de prueba con relaciones
        pasajeroConReserva = Pasajero.builder()
                .idPasajero(1L)
                .nombre("Juan Pérez")
                .nid("123456789")
                .build();

        Reserva reserva = Reserva.builder()
                .idReserva(1L)
                .codigoReserva(UUID.randomUUID())
                .pasajero(pasajeroConReserva)
                .build();

        pasajeroConReserva.getReservas().add(reserva);
    }

    @Test
    void createPasajero_DeberiaRetornarPasajeroCreado() {
        when(pasajeroService.createPasajero(any(Pasajero.class))).thenReturn(pasajeroConReserva);

        ResponseEntity<Pasajero> response = pasajeroController.createPasajero(pasajeroConReserva);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getIdPasajero());
        assertEquals("Juan Pérez", response.getBody().getNombre());
        verify(pasajeroService, times(1)).createPasajero(any(Pasajero.class));
    }

    @Test
    void getPasajeroById_DeberiaRetornarPasajeroConReservas() {
        when(pasajeroService.findById(1L)).thenReturn(Optional.of(pasajeroConReserva));

        ResponseEntity<Pasajero> response = pasajeroController.getPasajeroById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getReservas().isEmpty());
        assertEquals(1, response.getBody().getReservas().size());
        verify(pasajeroService, times(1)).findById(1L);
    }

    @Test
    void updatePasajero_DeberiaActualizarDatosBasicos() {
        Pasajero datosActualizados = Pasajero.builder()
                .nombre("Juan Pérez Actualizado")
                .nid("123456789")
                .build();

        when(pasajeroService.updatePasajero(anyLong(), any(Pasajero.class))).thenReturn(datosActualizados);

        ResponseEntity<Pasajero> response = pasajeroController.updatePasajero(1L, datosActualizados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Pérez Actualizado", response.getBody().getNombre());
        verify(pasajeroService, times(1)).updatePasajero(anyLong(), any(Pasajero.class));
    }

    @Test
    void getPasajerosPorReservaId_DeberiaRetornarPasajeroRelacionado() {
        when(pasajeroService.findByReservaId(1L)).thenReturn(List.of(pasajeroConReserva));

        ResponseEntity<List<Pasajero>> response = pasajeroController.getPasajerosPorReservaId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Juan Pérez", response.getBody().get(0).getNombre());
        verify(pasajeroService, times(1)).findByReservaId(1L);
    }

    @Test
    void getPasajerosConReservas_DeberiaFiltrarSoloConReservas() {
        Pasajero pasajeroSinReservas = Pasajero.builder()
                .idPasajero(2L)
                .nombre("Sin Reservas")
                .nid("00000000")
                .build();

        when(pasajeroService.encontrarPasajerosConReservas()).thenReturn(List.of(pasajeroConReserva));

        ResponseEntity<List<Pasajero>> response = pasajeroController.getPasajerosConReservas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertFalse(response.getBody().get(0).getReservas().isEmpty());
        verify(pasajeroService, times(1)).encontrarPasajerosConReservas();
    }

    @Test
    void deletePasajero_DeberiaEliminarConCascada() {
        doNothing().when(pasajeroService).deletePasajero(1L);

        ResponseEntity<Void> response = pasajeroController.deletePasajero(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(pasajeroService, times(1)).deletePasajero(1L);
    }

    @Test
    void searchPasajeros_DeberiaManejarMultiplesFiltros() {
        // Test para búsqueda por prefijo
        when(pasajeroService.findByNombreStartingWith("Ju")).thenReturn(List.of(pasajeroConReserva));
        ResponseEntity<List<Pasajero>> responsePrefijo = pasajeroController.searchPasajeros(null, null, "Ju");
        assertEquals(1, responsePrefijo.getBody().size());

        // Test para búsqueda por NID
        when(pasajeroService.findByNid("123456789")).thenReturn(List.of(pasajeroConReserva));
        ResponseEntity<List<Pasajero>> responseNid = pasajeroController.searchPasajeros(null, "123456789", null);
        assertEquals(1, responseNid.getBody().size());

        // Test para parámetros inválidos
        assertEquals(HttpStatus.BAD_REQUEST, pasajeroController.searchPasajeros(null, null, null).getStatusCode());
    }

    @Test
    void handlePasajeroNotFound_DeberiaRetornarMensajeClaro() {
        ResponseEntity<String> response = pasajeroController.handlePasajeroNotFound(
                new PasajeroNotFoundException(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("999"));
    }

    @Test
    void getNidMasCorto_DeberiaPriorizarLongitud() {
        Pasajero pasajeroCorto = Pasajero.builder()
                .nid("1")
                .nombre("Corto")
                .build();

        when(pasajeroService.encontrarPasajeroConNidMasCorto()).thenReturn(pasajeroCorto);

        ResponseEntity<Pasajero> response = pasajeroController.getNidMasCorto();

        assertEquals(1, response.getBody().getNid().length());
    }

    @Test
    void getNombreMasLargo_DeberiaPriorizarLongitud() {
        Pasajero pasajeroLargo = Pasajero.builder()
                .nombre("Nombre Super Largo de Prueba para el Test")
                .nid("123")
                .build();

        when(pasajeroService.encontrarPasajeroConNombreMasLargo()).thenReturn(pasajeroLargo);

        ResponseEntity<Pasajero> response = pasajeroController.getNombreMasLargo();

        assertTrue(response.getBody().getNombre().length() > 20);
    }

    @Test
    void getAllPasajeros_DeberiaRetornarListaVacia() {
        // Configurar servicio para retornar lista vacía
        when(pasajeroService.findByNombreContaining("")).thenReturn(List.of());

        // Ejecutar
        ResponseEntity<List<Pasajero>> respuesta = pasajeroController.getAllPasajeros();

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
        verify(pasajeroService, times(1)).findByNombreContaining("");
    }

    @Test
    void countByNombre_DeberiaRetornarCeroParaNombreInexistente() {
        // Configurar
        String nombreInexistente = "NombreFalso";
        when(pasajeroService.contarPorNombre(nombreInexistente)).thenReturn(0L);

        // Ejecutar
        ResponseEntity<Long> respuesta = pasajeroController.countByNombre(nombreInexistente);

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(0L, respuesta.getBody());
        verify(pasajeroService, times(1)).contarPorNombre(nombreInexistente);
    }

    @Test
    void getPasajerosIdGreaterThan_DeberiaManejarIdNegativo() {
        // Configurar ID inválido
        Long idInvalido = -1L;

        // Configurar servicio para retornar lista vacía
        when(pasajeroService.findByIdPasajeroGreaterThan(idInvalido)).thenReturn(List.of());

        // Ejecutar
        ResponseEntity<List<Pasajero>> respuesta = pasajeroController.getPasajerosIdGreaterThan(idInvalido);

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
        verify(pasajeroService, times(1)).findByIdPasajeroGreaterThan(idInvalido);
    }
}