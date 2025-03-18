package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Aerolinea;
import edu.unimagdalena.aerolineas.entities.Vuelo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class RepositoryVueloTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testPostgres")
            .withUsername("testUsername")
            .withPassword("testPassword");

    static {
        postgreSQLContainer.start();
    }

    Aerolinea aerolinea1;
    Aerolinea aerolinea2;
    Set<Aerolinea> aerolineas1 = new HashSet<>();
    Set<Aerolinea> aerolineas2 = new HashSet<>();
    Vuelo vuelo1;
    Vuelo vuelo2;
    Vuelo vuelo3;

    @Autowired
    RepositoryAerolinea aereolineaRepository;

    @Autowired
    RepositoryVuelo vueloRepository;

    @BeforeEach
    void setUp() {


        aerolinea1 = Aerolinea.builder().nombre("Jetsmart").build();
        aerolinea2 = Aerolinea.builder().nombre("Avianca").build();
        aerolineas1.add(aerolinea1);
        aerolineas1.add(aerolinea2);
        aerolineas2.add(aerolinea2);
        aereolineaRepository.saveAll(aerolineas1);
        aereolineaRepository.saveAll(aerolineas2);


        vuelo1 = vueloRepository.save(Vuelo.builder().numero(UUID.fromString("7145205c-6d70-4558-a704-962c45d11c55")).origen("Santa Marta").destino("Cartagena").aerolineas(new HashSet<>(List.of(aerolinea1, aerolinea2))).build());
        vuelo2 = vueloRepository.save(Vuelo.builder().numero(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).origen("Bogotá").destino("Santa Marta").aerolineas(new HashSet<>()).build());
        vuelo3 = vueloRepository.save(Vuelo.builder().numero(UUID.fromString("987e6543-e21a-43c9-b123-926614174999")).origen("Medellín").destino("Pereira").aerolineas(aerolineas2).build());
    }

    @AfterEach
    void tearDown() {

        aereolineaRepository.deleteAll();
        vueloRepository.deleteAll();
    }

    @Test
    void findByOrigen() {
        List<Vuelo> test = vueloRepository.findByOrigen("Bogotá");
        Assertions.assertFalse(test.isEmpty());
        Assertions.assertEquals("Santa Marta", test.get(0).getDestino());
    }

    @Test
    void findByDestino() {
        List<Vuelo> test = vueloRepository.findByDestino("Pereira");
        Assertions.assertFalse(test.isEmpty());
        Assertions.assertEquals("Medellín", test.get(0).getOrigen());
    }

    @Test
    void findByOrigenAndDestino() {
        List<Vuelo> test = vueloRepository.findByOrigenAndDestino("Santa Marta", "Cartagena");
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void findByIdVueloGreaterThan() {
        List<Vuelo> test = vueloRepository.findByIdVueloGreaterThan(2L);
        Assertions.assertNotNull(test);
        Assertions.assertFalse(test.isEmpty());

    }

    @Test
    void findByDestinoStartingWith() {
        List<Vuelo> test = vueloRepository.findByDestinoStartingWith("M");
        Assertions.assertTrue(test.isEmpty());
    }

    @Test
    void encontrarVuelosPorNumeroTerminandoEn() {

        List<Vuelo> vuelos = vueloRepository.encontrarVuelosPorNumeroTerminandoEn("999");

        Assertions.assertFalse(vuelos.isEmpty());
        Assertions.assertEquals(1, vuelos.size());
        Assertions.assertEquals(vuelo3.getNumero(), vuelos.get(0).getNumero());
    }

    @Test
    void encontrarVueloConDestinoMasLargo() {
        Vuelo vuelo = vueloRepository.encontrarVueloConDestinoMasLargo();
        Assertions.assertNotNull(vuelo);
        Assertions.assertEquals("Santa Marta", vuelo.getDestino());
    }

    @Test
    void encontrarPorNumeroQueContenga() {
        List<Vuelo> vuelos = vueloRepository.encontrarPorNumeroQueContenga("7145205c");
        Assertions.assertFalse(vuelos.isEmpty());
        Assertions.assertTrue(vuelos.stream().anyMatch(v -> v.getNumero().toString().contains("7145205c")));
    }

    @Test
    void contarPorOrigen() {
        long count = vueloRepository.contarPorOrigen("Santa Marta");
        Assertions.assertEquals(1, count);
    }

    @Test
    void encontrarVueloConOrigenMasLargo() {
        Vuelo vuelo = vueloRepository.encontrarVueloConOrigenMasLargo();
        Assertions.assertNotNull(vuelo);
        Assertions.assertEquals("Santa Marta", vuelo.getOrigen());
    }
}