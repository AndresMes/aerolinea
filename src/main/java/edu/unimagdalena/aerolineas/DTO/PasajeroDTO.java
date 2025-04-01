package edu.unimagdalena.aerolineas.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PasajeroDTO {

    private Long idPasajero;
    private String nombre;
    private String nid;

    public PasajeroDTO(String nombre, String nid) {
        this.nombre = nombre;
        this.nid = nid;
    }
}
