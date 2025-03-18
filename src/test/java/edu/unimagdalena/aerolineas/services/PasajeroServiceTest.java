package edu.unimagdalena.aerolineas.services;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.entities.Pasaporte;
import edu.unimagdalena.aerolineas.entities.Reserva;
import edu.unimagdalena.aerolineas.entities.Vuelo;
import edu.unimagdalena.aerolineas.exceptions.PasajeroNotFoundException;
import edu.unimagdalena.aerolineas.repositories.RepositoryPasajero;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PasajeroServiceTest {

    @Mock
    private RepositoryPasajero pasajeroRepository;

    @InjectMocks
    private PasajeroService pasajeroService;

    public PasajeroServiceTest(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createPasajero() {

        Pasajero pasajero = new Pasajero(null,"Pedro Picapiedra","7890", new Pasaporte(), null);
        when(pasajeroRepository.save(any(Pasajero.class))).thenReturn(new Pasajero(1L ,"Pedro Picapiedra","7890", new Pasaporte(), null));

        Pasajero createdPasajero = pasajeroService.createPasajero(pasajero);

        assertNotNull(createdPasajero.getIdPasajero());
        assertEquals("Pedro Picapiedra", createdPasajero.getNombre());
        assertEquals("7890", createdPasajero.getNid());
        verify(pasajeroRepository, times(1)).save(any(Pasajero.class));

    }

    @Test
    void findByNombre() {

        Pasajero test = new Pasajero(1L, "Juan Pedro", "1234", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of(test);
        when(pasajeroRepository.findByNombre(test.getNombre())).thenReturn(pasajeros);

        List<Pasajero> resultado = pasajeroService.findByNombre(test.getNombre());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pedro", resultado.get(0).getNombre());
        verify(pasajeroRepository, times(1)).findByNombre(test.getNombre());

    }

    @Test
    void findByNombreStartingWith() {

        Pasajero pasajero1 = new Pasajero(1L, "Juan Pedro", "1234", new Pasaporte(), null);
        Pasajero pasajero2 = new Pasajero(2L, "Juan Carlos", "5678", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of(pasajero1, pasajero2);

        when(pasajeroRepository.findByNombreStartingWith("Juan")).thenReturn(pasajeros);

        // Act
        List<Pasajero> resultado = pasajeroService.findByNombreStartingWith("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(pasajeroRepository, times(1)).findByNombreStartingWith("Juan");
    }

    @Test
    void findByNid() {

        Pasajero pasajero = new Pasajero(1L, "Pedro Luis", "1234", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of(pasajero);
        when(pasajeroRepository.findByNid("1234")).thenReturn(pasajeros);

        List<Pasajero> resultado = pasajeroService.findByNid("1234");

        assertEquals("Pedro Luis", resultado.get(0).getNombre());
        assertFalse(resultado.isEmpty());

        verify(pasajeroRepository, times(1)).findByNid("1234");
    }

    @Test
    void findByNombreContaining() {

        Pasajero pasajero1 = new Pasajero(1L, "Pedro Juan", "1234", new Pasaporte(), null);
        Pasajero pasajero2 = new Pasajero(2L, "Juan Luis", "5678", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of(pasajero1, pasajero2);
        when(pasajeroRepository.findByNombreContaining("Juan")).thenReturn(pasajeros);

        List<Pasajero> resultado = pasajeroService.findByNombreContaining("Juan");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(pasajeroRepository, times(1)).findByNombreContaining("Juan");
    }

    @Test
    void findByIdPasajeroGreaterThan() {

        Pasajero pasajero1 = new Pasajero(1L, "Pedro Juan", "1234", new Pasaporte(), null);
        Pasajero pasajero2 = new Pasajero(2L, "Juan Luis", "5678", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of();
        when(pasajeroRepository.findByIdPasajeroGreaterThan(4L)).thenReturn(pasajeros);

        List<Pasajero> resultado = pasajeroService.findByIdPasajeroGreaterThan(4L);

        assertTrue(resultado.isEmpty());

        verify(pasajeroRepository, times(1)).findByIdPasajeroGreaterThan(4L);


    }

    @Test
    void findByReservaId() {

        List<Reserva> reservas = List.of(new Reserva(1L, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), new Vuelo(), new Pasajero()));
        Pasajero pasajero1 =  new Pasajero(1L, "Pedro Juan", "1234", new Pasaporte(), reservas);
        List<Pasajero> pasajeros = List.of(pasajero1);
        when(pasajeroRepository.findByReservaId(1l)).thenReturn(pasajeros);

        List<Pasajero> resultado = pasajeroService.findByReservaId(1L);

        assertFalse(resultado.isEmpty());
        assertEquals("1234", resultado.get(0).getNid());

        verify(pasajeroRepository, times(1)).findByReservaId(1l);
    }

    @Test
    void encontrarPasajeroConNidMasCorto() {

        Pasajero pasajero1 = new Pasajero(1L, "Pedro Juan", "12345", new Pasaporte(), null);
        Pasajero pasajero2 = new Pasajero(2L, "Juan Luis", "5678", new Pasaporte(), null);
        when(pasajeroRepository.encontrarPasajeroConNidMasCorto()).thenReturn(pasajero2);

        Pasajero resultado = pasajeroService.encontrarPasajeroConNidMasCorto();

        assertEquals("5678", resultado.getNid()); // Verificar que el NID es el más corto
        assertEquals("Juan Luis", resultado.getNombre());

    }

    @Test
    void encontrarPasajerosConReservas() {

        Reserva reserva1 = new Reserva(1L, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), new Vuelo(), null);
        Reserva reserva2 = new Reserva(2L, UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), new Vuelo(), null);

        List<Reserva> reservas1 = List.of(reserva1);
        List<Reserva> reservas2 = List.of(reserva2);

        Pasajero pasajero1 = new Pasajero(1L, "Juan", "12345", new Pasaporte(), reservas1);
        Pasajero pasajero2 = new Pasajero(2L, "Pedro", "67890", new Pasaporte(), reservas2);

        List<Pasajero> pasajerosConReservas = List.of(pasajero1, pasajero2);

        when(pasajeroRepository.encontrarPasajerosConReservas()).thenReturn(pasajerosConReservas);


        List<Pasajero> resultado = pasajeroService.encontrarPasajerosConReservas();


        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(pasajeroRepository, times(1)).encontrarPasajerosConReservas();

    }

    @Test
    void contarPorNombre() {

        Pasajero pasajero1 = new Pasajero(1L, "Juan", "1234", new Pasaporte(), null);
        Pasajero pasajero2 = new Pasajero(2l, "Juan", "5678", new Pasaporte(), null);
        List<Pasajero> pasajeros = List.of(pasajero1, pasajero2);
        when(pasajeroRepository.contarPorNombre("Juan")).thenReturn(2L);

        Long resultado = pasajeroService.contarPorNombre("Juan");

        assertEquals(2, resultado);

        verify(pasajeroRepository, times(1)).contarPorNombre("Juan");

    }

    @Test
    void encontrarPasajeroConNombreMasLargo() {
        Pasajero pasajero1 = new Pasajero(1L, "Pedro Juan", "12345", new Pasaporte(), null);
        when(pasajeroRepository.encontrarPasajeroConNombreMasLargo()).thenReturn(pasajero1);

        Pasajero resultado = pasajeroService.encontrarPasajeroConNombreMasLargo();

        assertEquals("12345", resultado.getNid());
        assertEquals("Pedro Juan", resultado.getNombre());

        verify(pasajeroRepository, times(1)).encontrarPasajeroConNombreMasLargo();




    }

    @Test
    void findById_PasajeroExiste() {

        Pasajero pasajero = new Pasajero(1L, "Juan", "1234", new Pasaporte(), null);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));

        Optional<Pasajero> resultado = pasajeroService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdPasajero());
        assertEquals("Juan", resultado.get().getNombre());

        verify(pasajeroRepository, times(1)).findById(1L);
    }

    @Test
    void findById_PasajeroNoExiste() {

        when(pasajeroRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Pasajero> resultado = pasajeroService.findById(99L);

        assertFalse(resultado.isPresent());

        verify(pasajeroRepository, times(1)).findById(99L);
    }

    @Test
    void updatePasajero_PasajeroExiste() {
        Long id = 1L;
        Pasajero pasajeroExistente = new Pasajero(id, "Juan", "1234", new Pasaporte(), null);
        Pasajero pasajeroActualizado = new Pasajero(id, "Juan Pedro", "5678", new Pasaporte(), null);

        when(pasajeroRepository.findById(id)).thenReturn(Optional.of(pasajeroExistente));
        when(pasajeroRepository.save(pasajeroExistente)).thenReturn(pasajeroExistente);

        Pasajero resultado = pasajeroService.updatePasajero(id, pasajeroActualizado);

        assertNotNull(resultado);
        assertEquals("Juan Pedro", resultado.getNombre());
        assertEquals("5678", resultado.getNid());

        verify(pasajeroRepository, times(1)).findById(id);
        verify(pasajeroRepository, times(1)).save(pasajeroExistente);
    }

    @Test
    void updatePasajero_PasajeroNoExiste() {
        Long id = 99L;
        Pasajero pasajeroActualizado = new Pasajero(id, "Juan Pedro", "5678", new Pasaporte(), null);

        when(pasajeroRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PasajeroNotFoundException.class, () -> {
            pasajeroService.updatePasajero(id, pasajeroActualizado);
        });

        verify(pasajeroRepository, times(1)).findById(id);
        verify(pasajeroRepository, never()).save(any(Pasajero.class));
    }

    @Test
    void deletePasajero_PasajeroExiste() {
        Long id = 1L;
        when(pasajeroRepository.existsById(id)).thenReturn(true);

        pasajeroService.deletePasajero(id);

        verify(pasajeroRepository, times(1)).existsById(id);
        verify(pasajeroRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePasajero_PasajeroNoExiste() {
        Long id = 99L;
        when(pasajeroRepository.existsById(id)).thenReturn(false);

        assertThrows(PasajeroNotFoundException.class, () -> {
            pasajeroService.deletePasajero(id);
        });


        verify(pasajeroRepository, times(1)).existsById(id);
        verify(pasajeroRepository, never()).deleteById(id);
    }
}