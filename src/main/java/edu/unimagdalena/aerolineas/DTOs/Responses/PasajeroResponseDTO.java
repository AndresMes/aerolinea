package edu.unimagdalena.aerolineas.DTOs.Responses;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroResponseDTO {
    //servidor envía al cliente
    private Long idPasajero;
    private String nombre;
    private String nid;

}
