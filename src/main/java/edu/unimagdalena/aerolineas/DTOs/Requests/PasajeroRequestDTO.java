package edu.unimagdalena.aerolineas.DTOs.Requests;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroRequestDTO {
    //cliente envía al servidor
    private String nombre;
    private String nid;
}
