package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryPasajero extends JpaRepository<Pasajero, Long> {
    List<Pasajero> findByNombre(String nombre);

    List<Pasajero> findByNombreStartingWith(String prefijo);

    List<Pasajero> findByNid(String nid);

    List<Pasajero> findByNombreContaining(String fragmento);

    List<Pasajero> findByIdPasajeroGreaterThan(Long id);

    @Query("SELECT p FROM Pasajero p JOIN FETCH p.reservas WHERE p.nombre = :nombre")
    List<Pasajero> encontrarPasajerosConReservasPorNombre(@Param("nombre") String nombre);

    @Query("select p from Pasajero p order by length(p.nid) asc limit 1")
    Pasajero encontrarPasajeroConNidMasCorto();

    @Query("select p from Pasajero p where size(p.reservas) > 0")
    List<Pasajero> encontrarPasajerosConReservas();

    @Query("select count(p) from Pasajero p where p.nombre = ?1")
    long contarPorNombre(String nombre);

    @Query("select p from Pasajero p order by length(p.nombre) desc limit 1")
    Pasajero encontrarPasajeroConNombreMasLargo();

}
