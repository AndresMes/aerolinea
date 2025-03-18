package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Aerolinea;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryAerolineaTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testPostgres")
            .withUsername("testUsername")
            .withPassword("testPassword");

    @Autowired
    RepositoryAerolinea aerolineaRepository;

    Aerolinea aerolinea1;
    Aerolinea aerolinea2;
    Aerolinea aerolinea3;

    @BeforeEach
    void setUp() {
        aerolinea1 = Aerolinea.builder().nombre("Latam").build();
        aerolinea2 = Aerolinea.builder().nombre("Avianca").build();
        aerolinea3 = Aerolinea.builder().nombre("Viva Air").build();
        aerolineaRepository.saveAll(List.of(aerolinea1, aerolinea2, aerolinea3));
    }

    @AfterEach
    void tearDown() {
        aerolineaRepository.deleteAll();
    }

    @Test
    void findByIdAerolinea() {
        Long id = aerolinea1.getIdAerolinea();
        List<Aerolinea> aerolinea = aerolineaRepository.findByIdAerolinea(id);
        assertFalse(aerolinea.isEmpty());
        assertEquals("Latam", aerolinea.get(0).getNombre());
    }

    // Test CRUD básico
    @Test
    void testSaveAerolinea() {
        Aerolinea nueva = Aerolinea.builder().nombre("Nueva").build();
        Aerolinea guardada = aerolineaRepository.save(nueva);

        assertNotNull(guardada.getIdAerolinea());
        assertEquals("Nueva", guardada.getNombre());
    }

    @Test
    void testUpdateAerolinea() {
        aerolinea1.setNombre("Nuevo Nombre");
        Aerolinea actualizada = aerolineaRepository.save(aerolinea1);

        assertEquals("Nuevo Nombre", actualizada.getNombre());
    }

    @Test
    void testDeleteAerolinea() {
        Long id = aerolinea1.getIdAerolinea();
        aerolineaRepository.deleteById(id);

        assertFalse(aerolineaRepository.existsById(id));
    }

    // Tests para métodos derivados
    @Test
    void findByNombreEquals() {
        List<Aerolinea> result = aerolineaRepository.findByNombreEquals("Latam");

        assertEquals(1, result.size());
        assertEquals("Latam", result.get(0).getNombre());
    }

    @Test
    void findByIdAerolineaBetween() {
        List<Aerolinea> result = aerolineaRepository.findByIdAerolineaBetween(
                aerolinea1.getIdAerolinea(),aerolinea3.getIdAerolinea());
        assertTrue(result.size() >= 2);
    }

    @Test
    void findByNombreStartingWith() {
        List<Aerolinea> result = aerolineaRepository.findByNombreStartingWith("A");

        assertEquals(1, result.size());
        assertEquals("Avianca", result.get(0).getNombre());
    }

    @Test
    void findByNombreOrderByNombreAsc() {
        List<Aerolinea> result = aerolineaRepository.findByNombreOrderByNombreAsc("Avianca");

        assertEquals(1, result.size());
        assertEquals("Avianca", result.get(0).getNombre());
    }

    // Tests para queries JPQL personalizadas
    @Test
    void encontrarAerolineaDistintaPorNombre() {
        Aerolinea result = aerolineaRepository.encontrarAerolíneaDistintaPorNombre("Latam");

        assertNotNull(result);
        assertEquals("Latam", result.getNombre());
    }

    @Test
    void encontrarPorNombreQueContenga() {
        List<Aerolinea> result = aerolineaRepository.encontrarPorNombreQueContenga("Air");

        assertEquals(1, result.size());
        assertEquals("Viva Air", result.get(0).getNombre());
    }

    @Test
    void contarPorNombre() {
        long count = aerolineaRepository.contarPorNombre("Latam");

        assertEquals(1, count);
    }

    @Test
    void encontrarPorIdAerolineaMayorQueOrdenadoPorNombre() {
        List<Aerolinea> result = aerolineaRepository.encontrarPorIdAerolineaMayorQueOrdenadoPorNombre(
                aerolinea1.getIdAerolinea()
        );

        assertTrue(result.size() >= 2);
        assertTrue(result.get(0).getNombre().compareTo(result.get(1).getNombre()) <= 0);
    }

    @Test
    void encontrarAerolineaConNombreMasLargo() {
        // Agregar una aerolínea con nombre largo
        Aerolinea larga = Aerolinea.builder().nombre("Nombre Muy Largo de Aerolínea").build();
        aerolineaRepository.save(larga);

        Aerolinea result = aerolineaRepository.encontrarAerolineaConNombreMasLargo();

        assertNotNull(result);
        assertEquals("Nombre Muy Largo de Aerolínea", result.getNombre());
    }

    // Test adicional para empty results
    @Test
    void findByNombreEqualsNotFound() {
        List<Aerolinea> result = aerolineaRepository.findByNombreEquals("Inexistente");
        assertTrue(result.isEmpty());
    }
}
