package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.entities.Pasaporte;
import edu.unimagdalena.aerolineas.entities.Reserva;
import edu.unimagdalena.aerolineas.entities.Vuelo;
import org.junit.Assert;
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

class RepositoryReservaTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testPostgres")
            .withUsername("testUsername")
            .withPassword("testPassword");

    static {
        postgreSQLContainer.start();
    }

    Pasajero pasajero1;
    Pasajero pasajero2;
    Pasajero pasajero3;
    Set<Reserva> reservas1 = new HashSet<>();
    Set<Reserva> reservas2 = new HashSet<>();
    Set<Reserva> reservas3 = new HashSet<>();
    Reserva reserva1;
    Reserva reserva2;
    Reserva reserva3;
    Vuelo vuelo1;
    Vuelo vuelo2;
    Vuelo vuelo3;
    Vuelo vuelo4;
    Vuelo vuelo5;
    Pasaporte pasaporte1;
    Pasaporte pasaporte2;
    Pasaporte pasaporte3;

    @Autowired
    RepositoryPasajero pasajeroRepository;
    @Autowired
    RepositoryReserva reservaRepository;
    @Autowired
    RepositoryVuelo vueloRepository;
    @Autowired
    RepositoryPasaporte pasaporteRepository;

    @BeforeEach
    void setUp() {
        // 1. Crear y guardar Pasajeros primero
        pasajero1 = Pasajero.builder().nombre("Juan Francisco").nid("25A").build();
        pasajero2 = Pasajero.builder().nombre("Pedro Luan").nid("21B").build();
        pasajero3 = Pasajero.builder().nombre("Mateo Angulo").nid("25C").build();

        pasajeroRepository.save(pasajero1);
        pasajeroRepository.save(pasajero2);
        pasajeroRepository.save(pasajero3);

        // 2. Crear Pasaportes y asociar Pasajeros ya guardados
        pasaporte1 = Pasaporte.builder().numero("B894A").pasajero(pasajero1).build();
        pasaporte2 = Pasaporte.builder().numero("A578B").pasajero(pasajero2).build();
        pasaporte3 = Pasaporte.builder().numero("A588C").pasajero(pasajero3).build();

        // 3. Guardar Pasaportes (la cascada actualizará Pasajeros si es necesario)
        pasaporteRepository.save(pasaporte1);
        pasaporteRepository.save(pasaporte2);
        pasaporteRepository.save(pasaporte3);

        // 4. Guardar Vuelos
        vuelo1 = vueloRepository.save(Vuelo.builder().numero(UUID.randomUUID()).origen("Santa Marta").destino("Bogotá").build());
        vuelo2 = vueloRepository.save(Vuelo.builder().numero(UUID.randomUUID()).origen("Cartagena").destino("Medellín").build());
        vuelo3 = vueloRepository.save(Vuelo.builder().numero(UUID.randomUUID()).origen("Barranquilla").destino("Valledupar").build());
        vuelo4 = vueloRepository.save(Vuelo.builder().numero(UUID.randomUUID()).origen("Pereira").destino("Cucuta").build());
        vuelo5 = vueloRepository.save(Vuelo.builder().numero(UUID.randomUUID()).origen("Roma").destino("Barcelona").build());

        // 5. Crear y guardar Reservas
        reserva1 = reservaRepository.save(Reserva.builder()
                .codigoReserva(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .pasajero(pasajero1)
                .vuelo(vuelo1)
                .build());
        reserva2 = reservaRepository.save(Reserva.builder()
                .codigoReserva(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .pasajero(pasajero1)
                .vuelo(vuelo2)
                .build());
        reserva3 = reservaRepository.save(Reserva.builder()
                .codigoReserva(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                .pasajero(pasajero1)
                .vuelo(vuelo3)
                .build());

        // 6. Actualizar listas de reservas
        reservas1.add(reserva1);
        reservas1.add(reserva2);
        reservas1.add(reserva3);
        reservas2.add(reserva3);
        reservas2.add(reserva1);
    }

    @AfterEach
    void tearDown() {
        reservaRepository.deleteAll();
        pasaporteRepository.deleteAll();
        pasajeroRepository.deleteAll();
        vueloRepository.deleteAll();
    }

    @Test
    void findByIdReservaGreaterThan() {
        List<Reserva> test = reservaRepository.findByIdReservaGreaterThan(3L);
        Assertions.assertNotNull(test);

        System.out.println("ID reserva1:" + reserva1.getIdReserva());
        System.out.println("ID reserva2:" + reserva2.getIdReserva());
        System.out.println("ID reserva3:" + reserva3.getIdReserva());

        if(reserva1.getIdReserva() == 1){
            Assertions.assertEquals(0, test.size());
        }
        else{
            Assertions.assertEquals(3, test.size());
        }

    }

    @Test
    void findByIdReservaBetween() {
        List<Reserva> reservas = reservaRepository.findByIdReservaBetween(1L, 2L);

        System.out.println("ID reserva1: " + reserva1.getIdReserva());
        System.out.println("ID reserva2: " + reserva2.getIdReserva());

        if(reserva1.getIdReserva() == 1) {

            Assertions.assertEquals(2, reservas.size());
            Assertions.assertTrue(reservas.contains(reserva1));
            Assertions.assertTrue(reservas.contains(reserva2));
            Assertions.assertFalse(reservas.contains(reserva3));

        }else{
            Assertions.assertEquals(0, reservas.size());
        }
    }

    @Test
    void findByCodigoReservaIn() {
        List<UUID> codigosReserva = List.of(
                reserva1.getCodigoReserva(),
                reserva3.getCodigoReserva()
        );
        List<Reserva> reservas = reservaRepository.findByCodigoReservaIn(codigosReserva);
        assertEquals(2, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertTrue(reservas.contains(reserva3));
        assertFalse(reservas.contains(reserva2));
    }

    @Test
    void existsByCodigoReserva() {
        UUID codigoReserva = reserva2.getCodigoReserva();
        boolean existe = reservaRepository.existsByCodigoReserva(codigoReserva);
        assertTrue(existe);
    }

    @Test
    void findByVueloIdVuelo() {
        Long idVuelo = vuelo1.getIdVuelo();
        List<Reserva> reservas = reservaRepository.findByVueloIdVuelo(idVuelo);
        assertEquals(1, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertFalse(reservas.contains(reserva2));
        assertFalse(reservas.contains(reserva3));
    }

    @Test
    void buscarCodigosMayoresQue() {

        UUID codigoReferencia = reserva1.getCodigoReserva();

        // Ejecutar la consulta
        List<Reserva> reservas = reservaRepository.buscarCodigosMayoresQue(codigoReferencia);

        // Verificar el resultado
        assertEquals(2, reservas.size()); // Debería devolver reserva2 y reserva3 (códigos mayores)

        // Verificar que las reservas devueltas tengan códigos mayores que el de referencia
        for (Reserva reserva : reservas) {
            assertTrue(reserva.getCodigoReserva().compareTo(codigoReferencia) > 0);
        }

        // Verificar que reserva1 no esté en la lista
        assertFalse(reservas.contains(reserva1));
    }

    @Test
    void buscarPorIdPasajero() {
        Long idPasajero = pasajero1.getIdPasajero();
        List<Reserva> reservas = reservaRepository.buscarPorIdPasajero(idPasajero);

        assertEquals(3, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertTrue(reservas.contains(reserva2));
        assertTrue(reservas.contains(reserva3));
    }

    @Test
    void buscarPorVueloYPasajero() {
        Long idVuelo = vuelo1.getIdVuelo();
        Long idPasajero = pasajero1.getIdPasajero();
        List<Reserva> reservas = reservaRepository.buscarPorVueloYPasajero(idVuelo, idPasajero);

        assertEquals(1, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertFalse(reservas.contains(reserva2));
        assertFalse(reservas.contains(reserva3));
    }

    @Test
    void buscarCodigoContiene() {
        String fragmento = "3be0e7b7";
        List<Reserva> reservas = reservaRepository.buscarCodigoContiene(fragmento);
        assertEquals(1, reservas.size());
        assertTrue(reservas.contains(reserva3));
        assertFalse(reservas.contains(reserva1));
        assertFalse(reservas.contains(reserva2));
    }

    @Test
    void buscarUltimas5Reservas() {
        List<Reserva> reservas = reservaRepository.buscarUltimas5Reservas();
        assertEquals(3, reservas.size());
        assertEquals(reserva3.getIdReserva(), reservas.get(0).getIdReserva());
        assertEquals(reserva2.getIdReserva(), reservas.get(1).getIdReserva());
        assertEquals(reserva1.getIdReserva(), reservas.get(2).getIdReserva());
    }


}