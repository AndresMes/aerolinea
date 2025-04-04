package edu.unimagdalena.aerolineas.services.interfaces;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Declaramos que es servicio
@Service
public interface PasajeroService {
    Pasajero createPasajero(Pasajero pasajero);
    List<Pasajero> findByNombre(String nombre);
    List<Pasajero> findByNombreStartingWith(String prefijo);
    List<Pasajero> findByNid(String nid);
    List<Pasajero> findByNombreContaining(String fragmento);
    List<Pasajero> findByIdPasajeroGreaterThan(Long id);
    List<Pasajero> findByReservaId(Long reservaId);
    Pasajero encontrarPasajeroConNidMasCorto();
    List<Pasajero> encontrarPasajerosConReservas();
    long contarPorNombre(String nombre);
    Pasajero encontrarPasajeroConNombreMasLargo();
    Optional<Pasajero> findById(Long id);
    Pasajero updatePasajero(Long id, Pasajero pasajeroActualizado);
    void deletePasajero(Long id);
}

