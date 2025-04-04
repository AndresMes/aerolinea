package edu.unimagdalena.aerolineas.DTO.mappers;

import edu.unimagdalena.aerolineas.DTO.PasajeroResponseDTO;
import edu.unimagdalena.aerolineas.entities.Pasajero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasajeroMapper {

    // Mapeo de Entidad a DTO de respuesta
    @Mapping(source = "idPasajero", target = "idPasajero")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "nid", target = "nid")
    PasajeroResponseDTO pasajeroToResponseDto(Pasajero pasajero);


    Pasajero dtoToPasajero(PasajeroResponseDTO pasajeroDto);

}
