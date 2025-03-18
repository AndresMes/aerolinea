package edu.unimagdalena.aerolineas.controllers;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.exceptions.PasajeroNotFoundException;
import edu.unimagdalena.aerolineas.services.PasajeroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    @PostMapping
    public ResponseEntity<Pasajero> createPasajero(@RequestBody Pasajero pasajero) {
        Pasajero nuevoPasajero = pasajeroService.createPasajero(pasajero);
        return new ResponseEntity<>(nuevoPasajero, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pasajero>> getAllPasajeros() {
        List<Pasajero> pasajeros = pasajeroService.findByNombreContaining("");
        return ResponseEntity.ok(pasajeros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pasajero> getPasajeroById(@PathVariable Long id) {
        Pasajero pasajero = pasajeroService.findById(id)
                .orElseThrow(() -> new PasajeroNotFoundException(id));
        return ResponseEntity.ok(pasajero);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Pasajero>> searchPasajeros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nid,
            @RequestParam(required = false) String prefijo) {

        if (nombre != null) {
            return ResponseEntity.ok(pasajeroService.findByNombre(nombre));
        }
        if (nid != null) {
            return ResponseEntity.ok(pasajeroService.findByNid(nid));
        }
        if (prefijo != null) {
            return ResponseEntity.ok(pasajeroService.findByNombreStartingWith(prefijo));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/con-reservas")
    public ResponseEntity<List<Pasajero>> getPasajerosConReservas() {
        List<Pasajero> pasajeros = pasajeroService.encontrarPasajerosConReservas();
        return ResponseEntity.ok(pasajeros);
    }

    @GetMapping("/estadisticas/nid-mas-corto")
    public ResponseEntity<Pasajero> getNidMasCorto() {
        Pasajero pasajero = pasajeroService.encontrarPasajeroConNidMasCorto();
        return ResponseEntity.ok(pasajero);
    }

    @GetMapping("/estadisticas/nombre-mas-largo")
    public ResponseEntity<Pasajero> getNombreMasLargo() {
        Pasajero pasajero = pasajeroService.encontrarPasajeroConNombreMasLargo();
        return ResponseEntity.ok(pasajero);
    }

    @ExceptionHandler(PasajeroNotFoundException.class)
    public ResponseEntity<String> handlePasajeroNotFound(PasajeroNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidInput(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping("/por-reserva/{reservaId}")
    public ResponseEntity<List<Pasajero>> getPasajerosPorReservaId(
            @PathVariable Long reservaId) {
        List<Pasajero> pasajeros = pasajeroService.findByReservaId(reservaId);
        return ResponseEntity.ok(pasajeros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pasajero> updatePasajero(
            @PathVariable Long id,
            @RequestBody Pasajero pasajero) {
        Pasajero actualizado = pasajeroService.updatePasajero(id, pasajero);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasajero(@PathVariable Long id) {
        pasajeroService.deletePasajero(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/greater-than/{id}")
    public ResponseEntity<List<Pasajero>> getPasajerosIdGreaterThan(
            @PathVariable Long id) {
        List<Pasajero> pasajeros = pasajeroService.findByIdPasajeroGreaterThan(id);
        return ResponseEntity.ok(pasajeros);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByNombre(
            @RequestParam String nombre) {
        Long count = pasajeroService.contarPorNombre(nombre);
        return ResponseEntity.ok(count);
    }
}