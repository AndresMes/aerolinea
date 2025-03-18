package edu.unimagdalena.aerolineas.exceptions;

public class PasajeroNotFoundException extends RuntimeException {
    public PasajeroNotFoundException(Long id) {
        super("No se encontró el pasajero con ID: " + id);
    }
}