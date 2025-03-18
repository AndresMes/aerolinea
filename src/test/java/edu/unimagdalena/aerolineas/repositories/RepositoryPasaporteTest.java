package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.entities.Pasaporte;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryPasaporteTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testPostgres")
            .withUsername("testUsername")
            .withPassword("testPassword");

    static {
        postgreSQLContainer.start();
    }

    Pasaporte pasaporte1;
    Pasaporte pasaporte2;
    Pasaporte pasaporte3;
    Pasajero pasajero1;
    Pasajero pasajero2;
    Pasajero pasajero3;

    @Autowired
    RepositoryPasajero pasajeroRepository;
    @Autowired
    RepositoryPasaporte pasaporteRepository;

    @BeforeEach
    void setUp() {
        // Crear Pasajeros
        pasajero1 = Pasajero.builder().nombre("Juan Pedro").nid("25A").build();
        pasajero2 = Pasajero.builder().nombre("Juan Diego").nid("21B").build();
        pasajero3 = Pasajero.builder().nombre("Andrés Federico").nid("25C").build();

        // Crear Pasaportes
        pasaporte1 = Pasaporte.builder().numero("489478AS").pasajero(pasajero1).build();
        pasaporte2 = Pasaporte.builder().numero("A578B").pasajero(pasajero2).build();
        pasaporte3 = Pasaporte.builder().numero("A588C").pasajero(pasajero3).build();

        // Guardar Pasaportes primero
        pasaporteRepository.save(pasaporte1);
        pasaporteRepository.save(pasaporte2);
        pasaporteRepository.save(pasaporte3);

        // Asociar bidireccionalmente y guardar Pasajeros
        pasajero1.setPasaporte(pasaporte1);
        pasajero2.setPasaporte(pasaporte2);
        pasajero3.setPasaporte(pasaporte3);

        pasajeroRepository.save(pasajero1);
        pasajeroRepository.save(pasajero2);
        pasajeroRepository.save(pasajero3);
    }

    @AfterEach
    void tearDown() {
        //Elimina los datos despues de cada de test
        pasaporteRepository.deleteAll();
        pasajeroRepository.deleteAll();
    }


    @Test
    void findByNumero() {
        Pasaporte test = pasaporteRepository.findByNumero("A578B");
        Assertions.assertNotNull(test);
        Assertions.assertEquals("A578B", test.getNumero());
    }

    @Test
    void findByNumeroBetween() {
        List<Pasaporte> test = pasaporteRepository.findByNumeroBetween("A578B", "A587C");
        Assertions.assertNotNull(test);
        Assertions.assertEquals(1, test.size());
    }

    @Test
    void findByIdPasaporteBefore() {
        List<Pasaporte> test = pasaporteRepository.findByIdPasaporteBefore(2L);
        Assertions.assertNotNull(test);
        if(pasaporte1.getIdPasaporte() == 1){
            Assertions.assertEquals(1, test.size());
        }else{
            Assertions.assertEquals(0, test.size());
        }

    }

    @Test
    void findByNumeroAfter() {
        List<Pasaporte> test = pasaporteRepository.findByNumeroAfter("A577B");
        Assertions.assertNotNull(test);
        Assertions.assertEquals(2, test.size());
    }

    @Test
    void findByNumeroBetweenAndNumeroAfter() {
        List<Pasaporte> test = pasaporteRepository.findByNumeroBetweenAndNumeroAfter("A577B", "A589C", "A578B");

        Assertions.assertNotNull(test);
        Assertions.assertFalse(test.isEmpty());
    }

    @Test
    void buscarPasaportesComenzarEnDos() {
        List<Pasaporte> test = pasaporteRepository.buscarPasaportesComenzarEnDos();
        Assertions.assertTrue(test.isEmpty());

    }

    @Test
    void buscarTodosPasaportes() {
        List<Pasaporte> test = pasaporteRepository.buscarTodosPasaportes();
        Assertions.assertNotNull(test);
        Assertions.assertFalse(test.isEmpty());

    }

    @Test
    void buscarPasaporteRango() {

        List<Pasaporte> test = pasaporteRepository.findByNumeroBetween("A578B", "A587C");
        Assertions.assertNotNull(test);
        Assertions.assertEquals(1, test.size());

    }

    @Test
    void buscarPasaporteMayoresCuatro() {

        System.out.println("Pasaporte 1 ID: " + pasaporte1.getIdPasaporte());
        System.out.println("Pasaporte 2 ID: " + pasaporte2.getIdPasaporte());
        System.out.println("Pasaporte 3 ID: " + pasaporte3.getIdPasaporte());

        List<Pasaporte> test = pasaporteRepository.buscarPasaporteMayoresCuatro();
        Assertions.assertNotNull(test);
        if(pasaporte1.getIdPasaporte() == 0){
            Assertions.assertEquals(0, test.size());
        }else{
            Assertions.assertEquals(3, test.size());
        }

    }

    @Test
    void buscarPasaporteEntreUnoYCien() {
        List<Pasaporte> test = pasaporteRepository.buscarPasaporteEntreUnoYCien();
        Assertions.assertNotNull(test);
        Assertions.assertEquals(3, test.size());
    }
}