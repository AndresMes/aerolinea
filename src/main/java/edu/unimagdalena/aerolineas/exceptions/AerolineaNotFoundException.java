package edu.unimagdalena.aerolineas.exceptions;

public class AerolineaNotFoundException extends RuntimeException {
    public AerolineaNotFoundException(Long id) {
        super("No se encontró la aerolinea con el ID: " + id);
    }
}
