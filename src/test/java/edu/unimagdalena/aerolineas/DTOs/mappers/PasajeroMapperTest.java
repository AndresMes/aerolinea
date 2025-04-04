package edu.unimagdalena.aerolineas.DTOs.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import edu.unimagdalena.aerolineas.entities.Pasaporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.unimagdalena.aerolineas.DTOs.Requests.PasajeroRequestDTO;
import edu.unimagdalena.aerolineas.DTOs.Responses.PasajeroResponseDTO;
import edu.unimagdalena.aerolineas.entities.Pasajero;
import static org.junit.jupiter.api.Assertions.*;

public class PasajeroMapperTest {
    private PasajeroMapper pasajeroMapper;

    @BeforeEach
    void setUp() {
        pasajeroMapper = new PasajeroMapperImpl();
    }

    @Test
    void testPasajeroToResponseDto() {
        // Arrange
        Pasajero pasajero = Pasajero.builder()
                .idPasajero(1L)
                .nombre("Juan Pérez")
                .nid("ABC123")
                .build();

        // Act
        PasajeroResponseDTO responseDTO = pasajeroMapper.pasajeroToResponseDto(pasajero);

        // Assert
        assertThat(responseDTO.getIdPasajero()).isEqualTo(1L);
        assertThat(responseDTO.getNombre()).isEqualTo("Juan Pérez");
        assertThat(responseDTO.getNid()).isEqualTo("ABC123");
    }

    @Test
    void testRequestDtoToPasajero() {
        // Arrange
        PasajeroRequestDTO requestDTO = PasajeroRequestDTO.builder()
                .nombre("Ana López")
                .nid("XYZ789")
                .build();

        // Act
        Pasajero pasajero = pasajeroMapper.requestDtoToPasajero(requestDTO);

        // Assert
        assertThat(pasajero.getIdPasajero()).isNull(); // Ignorado
        assertThat(pasajero.getNombre()).isEqualTo("Ana López");
        assertThat(pasajero.getNid()).isEqualTo("XYZ789");
        assertThat(pasajero.getPasaporte()).isNull(); // Ignorado
        assertThat(pasajero.getReservas()).isEmpty();  // Ignorado
    }

    @Test
    void testUpdatePasajeroFromRequestDto() {
        // Arrange
        PasajeroRequestDTO requestDTO = PasajeroRequestDTO.builder()
                .nombre("Nombre Actualizado")
                .nid("NID456")
                .build();

        // Crear pasaporte válido
        Pasaporte pasaporteExistente = Pasaporte.builder()
                .idPasaporte(1L)
                .numero("PASSPORT123")
                .build();

        Pasajero pasajeroExistente = Pasajero.builder()
                .idPasajero(1L)
                .nombre("Nombre Original")
                .nid("NID123")
                .pasaporte(pasaporteExistente)
                .build();

        // Establecer relación bidireccional
        pasaporteExistente.setPasajero(pasajeroExistente);

        // Act
        pasajeroMapper.updatePasajeroFromRequestDto(requestDTO, pasajeroExistente);

        // Assert
        assertThat(pasajeroExistente.getIdPasajero()).isEqualTo(1L);
        assertThat(pasajeroExistente.getNombre()).isEqualTo("Nombre Actualizado");
        assertThat(pasajeroExistente.getNid()).isEqualTo("NID456");

        // Verificar que el pasaporte no se modificó
        assertThat(pasajeroExistente.getPasaporte()).isNotNull();
        assertThat(pasajeroExistente.getPasaporte().getNumero()).isEqualTo("PASSPORT123");
        assertThat(pasajeroExistente.getPasaporte().getPasajero()).isEqualTo(pasajeroExistente);
    }

    @Test
    void testResponseDtoToPasajero() {
        // Arrange
        PasajeroResponseDTO responseDTO = PasajeroResponseDTO.builder()
                .idPasajero(2L)
                .nombre("Carlos Sánchez")
                .nid("DEF456")
                .build();

        // Act
        Pasajero pasajero = pasajeroMapper.responseDtoToPasajero(responseDTO);

        // Assert
        assertThat(pasajero.getIdPasajero()).isEqualTo(2L);
        assertThat(pasajero.getNombre()).isEqualTo("Carlos Sánchez");
        assertThat(pasajero.getNid()).isEqualTo("DEF456");
        assertThat(pasajero.getPasaporte()).isNull(); // Ignorado
        assertThat(pasajero.getReservas()).isEmpty();  // Ignorado
    }
}