package edu.unimagdalena.aerolineas.DTO.mappers;

import edu.unimagdalena.aerolineas.DTO.PasajeroDTO;
import edu.unimagdalena.aerolineas.entities.Pasajero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasajeroMapper {

    @Mapping(source = "idPasajero", target = "idPasajero")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "nid", target = "nid")
    PasajeroDTO pasajeroToDto(Pasajero pasajero);

    Pasajero dtoToPasajero(PasajeroDTO pasajeroDto);


}
