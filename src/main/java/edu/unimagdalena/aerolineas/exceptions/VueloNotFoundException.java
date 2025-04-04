package edu.unimagdalena.aerolineas.exceptions;

public class VueloNotFoundException extends RuntimeException {
    public VueloNotFoundException(Long id) {
        super("Vuelo con ID " + id + " no encontrado");
    }
}