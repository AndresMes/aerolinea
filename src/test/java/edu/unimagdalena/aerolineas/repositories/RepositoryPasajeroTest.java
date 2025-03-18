package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.entities.Reserva;
import edu.unimagdalena.aerolineas.entities.Vuelo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryPasajeroTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testPostgres")
            .withUsername("testUsername")
            .withPassword("testPassword");

    @Autowired
    RepositoryPasajero pasajeroRepository;

    @Autowired
    RepositoryReserva reservaRepository;

    @Autowired
    RepositoryVuelo vueloRepository;

    Pasajero pasajero1;
    Pasajero pasajero2;
    Pasajero pasajero3;

    @BeforeEach
    void setUp() {
        pasajero1 = Pasajero.builder()
                .nombre("Juan Pérez")
                .nid("123456789")
                .build();
        pasajeroRepository.save(pasajero1);

        pasajero2 = Pasajero.builder()
                .nombre("María García")
                .nid("456123789")
                .build();
        pasajeroRepository.save(pasajero2);

        pasajero3 = Pasajero.builder()
                .nombre("Carlos Airton")
                .nid("789456123")
                .build();
        pasajeroRepository.save(pasajero3);

        Vuelo vuelo = Vuelo.builder()
                .numero(UUID.randomUUID())
                .origen("BOG")
                .destino("MDE")
                .build();
        vueloRepository.save(vuelo);

        // Crear Reserva y asociarla al pasajero
        Reserva reserva = Reserva.builder()
                .codigoReserva(UUID.randomUUID())
                .pasajero(pasajero1)
                .vuelo(vuelo)
                .build();
        reservaRepository.save(reserva);

        // Actualizar ambos lados de la relación
        pasajero1.getReservas().add(reserva);
        pasajeroRepository.save(pasajero1);
    }


    @AfterEach
    void tearDown() {
        reservaRepository.deleteAll();
        pasajeroRepository.deleteAll();
        vueloRepository.deleteAll();

    }

    // Test CRUD básico
    @Test
    void testSavePasajero() {
        Pasajero nuevo = Pasajero.builder()
                .nombre("Nuevo Pasajero")
                .nid("000000000")
                .build();

        Pasajero guardado = pasajeroRepository.save(nuevo);

        assertNotNull(guardado.getIdPasajero());
        assertEquals("Nuevo Pasajero", guardado.getNombre());
    }

    @Test
    void testUpdatePasajero() {
        pasajero1.setNombre("Nombre Actualizado");
        Pasajero actualizado = pasajeroRepository.save(pasajero1);

        assertEquals("Nombre Actualizado", actualizado.getNombre());
    }

    @Test
    void testDeletePasajero() {
        Long id = pasajero1.getIdPasajero();

        // 1. Obtener el pasajero (esto carga las reservas si están en el contexto de persistencia)
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Pasajero no encontrado"));

        // 2. Eliminar el pasajero (las reservas se eliminarán en cascada)
        pasajeroRepository.delete(pasajero);

        // 3. Verificar que ya no existe
        assertFalse(pasajeroRepository.existsById(id));
    }

    // Tests para métodos derivados
    @Test
    void findByNombre() {
        List<Pasajero> result = pasajeroRepository.findByNombre("Juan Pérez");

        assertEquals(1, result.size());
        assertEquals("123456789", result.get(0).getNid());
    }

    @Test
    void findByNombreStartingWith() {
        List<Pasajero> result = pasajeroRepository.findByNombreStartingWith("Mar");
        assertEquals(1, result.size());
        assertEquals("María García", result.get(0).getNombre());
    }

    @Test
    void findByNid() {
        List<Pasajero> result = pasajeroRepository.findByNid("456123789");
        assertEquals(1, result.size());
        assertEquals("María García", result.get(0).getNombre());
    }

    @Test
    void findByNombreContaining() {
        Long idPasajero1 = pasajero1.getIdPasajero();
        List<Pasajero> result = pasajeroRepository.findByIdPasajeroGreaterThan(idPasajero1);
        assertEquals(2, result.size()); // Debería retornar pasajero2 y pasajero3
    }

    @Test
    void findByIdPasajeroGreaterThan() {
        List<Pasajero> result = pasajeroRepository.findByIdPasajeroGreaterThan(pasajero1.getIdPasajero());

        assertTrue(result.size() >= 2);
    }

    // Tests para queries JPQL personalizadas
    @Test
    void findByReservaId() {
        // Obtener la reserva creada en el setUp
        List<Reserva> reservas = reservaRepository.findAll();
        assertFalse(reservas.isEmpty());
        Long reservaId = reservas.get(0).getIdReserva();

        // Ejecutar la consulta
        List<Pasajero> result = pasajeroRepository.findByReservaId(reservaId);

        // Verificar resultados
        assertEquals(1, result.size());
        assertEquals(pasajero1.getIdPasajero(), result.get(0).getIdPasajero());
        assertEquals("Juan Pérez", result.get(0).getNombre());
    }

    @Test
    void findByReservaId_NotFound() {
        // ID de reserva inexistente
        List<Pasajero> result = pasajeroRepository.findByReservaId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void encontrarPasajeroConNidMasCorto() {
        // Agregar pasajero con NID corto
        Pasajero corto = Pasajero.builder()
                .nombre("Corto")
                .nid("1")
                .build();
        pasajeroRepository.save(corto);

        Pasajero result = pasajeroRepository.encontrarPasajeroConNidMasCorto();

        assertNotNull(result);
        assertEquals("1", result.getNid());
    }

    @Test
    void encontrarPasajerosConReservas() {
        List<Pasajero> result = pasajeroRepository.encontrarPasajerosConReservas();

        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
    }

    @Test
    void contarPorNombre() {
        long count = pasajeroRepository.contarPorNombre("Juan Pérez");

        assertEquals(1, count);
    }

    @Test
    void encontrarPasajeroConNombreMasLargo() {
        // Agregar pasajero con nombre largo
        Pasajero largo = Pasajero.builder()
                .nombre("Nombre Muy Largo de Pasajero")
                .nid("999999999")
                .build();
        pasajeroRepository.save(largo);

        Pasajero result = pasajeroRepository.encontrarPasajeroConNombreMasLargo();

        assertNotNull(result);
        assertEquals("Nombre Muy Largo de Pasajero", result.getNombre());
    }

    // Tests adicionales
    @Test
    void findByNombreNotFound() {
        List<Pasajero> result = pasajeroRepository.findByNombre("Inexistente");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNidNotFound() {
        List<Pasajero> result = pasajeroRepository.findByNid("000000000");
        assertTrue(result.isEmpty());
    }
}