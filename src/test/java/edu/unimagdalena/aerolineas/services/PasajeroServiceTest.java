package edu.unimagdalena.aerolineas.services;

import edu.unimagdalena.aerolineas.repositories.RepositoryPasajero;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


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
    }

    @Test
    void findByNombre() {
    }

    @Test
    void findByNombreStartingWith() {
    }

    @Test
    void findByNid() {
    }

    @Test
    void findByNombreContaining() {
    }

    @Test
    void findByIdPasajeroGreaterThan() {
    }

    @Test
    void findByReservaId() {
    }

    @Test
    void encontrarPasajeroConNidMasCorto() {
    }

    @Test
    void encontrarPasajerosConReservas() {
    }

    @Test
    void contarPorNombre() {
    }

    @Test
    void encontrarPasajeroConNombreMasLargo() {
    }

    @Test
    void findById() {
    }

    @Test
    void updatePasajero() {
    }

    @Test
    void deletePasajero() {
    }
}