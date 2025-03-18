package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RepositoryReserva extends JpaRepository<Reserva, Long> {


    List<Reserva> findByIdReservaGreaterThan(Long numero);
    List<Reserva> findByIdReservaBetween(Long min, Long max);
    List<Reserva> findByCodigoReservaIn(List<UUID> codigoReservas);
    boolean existsByCodigoReserva(UUID codigo);
    List<Reserva> findByVueloIdVuelo(Long idVuelo);

    // ========== 5 MÉTODOS JPQL ==========
    @Query("SELECT r FROM Reserva r WHERE r.codigoReserva > ?1")
    List<Reserva> buscarCodigosMayoresQue(UUID codigoReferencia);

    @Query("SELECT r FROM Reserva r WHERE r.pasajero.idPasajero = ?1")
    List<Reserva> buscarPorIdPasajero(Long idPasajero);

    @Query("SELECT r FROM Reserva r WHERE r.vuelo.idVuelo = ?1 AND r.pasajero.idPasajero = ?2")
    List<Reserva> buscarPorVueloYPasajero(Long idVuelo, Long idPasajero);

    @Query("SELECT r FROM Reserva r WHERE CAST(r.codigoReserva AS string) LIKE %?1%")
    List<Reserva> buscarCodigoContiene(String fragmento);

    @Query("SELECT r FROM Reserva r ORDER BY r.idReserva DESC LIMIT 5")
    List<Reserva> buscarUltimas5Reservas();
}
