package edu.unimagdalena.aerolineas.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroResponseDTO {

    private Long idPasajero;
    private String nombre;
    private String nid;

}
