package edu.unimagdalena.aerolineas.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroRequestDTO {
    private String nombre;
    private String nid;
}
