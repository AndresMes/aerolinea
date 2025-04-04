package edu.unimagdalena.aerolineas.DTOs.mappers;

import edu.unimagdalena.aerolineas.DTOs.Requests.PasajeroRequestDTO;
import edu.unimagdalena.aerolineas.DTOs.Responses.PasajeroResponseDTO;
import edu.unimagdalena.aerolineas.entities.Pasajero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PasajeroMapper {

    // Entidad -> ResponseDTO (Para consultas)
    PasajeroResponseDTO pasajeroToResponseDto(Pasajero pasajero);
    /*
    Antes
    @Mapping(source = "idPasajero", target = "idPasajero")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "nid", target = "nid")
    MapStruct mapea automáticamente los campos con el mismo nombre.
     */

    // RequestDTO -> Entidad (Para creación)
    @Mapping(target = "idPasajero", ignore = true)
    @Mapping(target = "pasaporte", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Pasajero requestDtoToPasajero(PasajeroRequestDTO dto);

    // Actualización Entidad desde RequestDTO
    @Mapping(target = "idPasajero", ignore = true)
    @Mapping(target = "pasaporte", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    void updatePasajeroFromRequestDto(PasajeroRequestDTO dto, @MappingTarget Pasajero pasajero);

    //ResponseDTO -> Entidad
    @Mapping(target = "pasaporte", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Pasajero responseDtoToPasajero(PasajeroResponseDTO dto);
}