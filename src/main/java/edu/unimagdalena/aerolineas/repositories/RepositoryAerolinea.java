package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryAerolinea extends JpaRepository<Aerolinea, Long> {
    List<Aerolinea> findByIdAerolinea(Long idAerolinea);

    List<Aerolinea> findByNombreEquals(String nombre);

    List<Aerolinea> findByIdAerolineaBetween(Long idAerolinea1, Long idAerolinea2);

    List<Aerolinea> findByNombreStartingWith(String nombre);

    List<Aerolinea> findByNombreOrderByNombreAsc(String nombre);

    @Query("select distinct u from Aerolinea u where u.nombre = ?1")
    Aerolinea encontrarAerolíneaDistintaPorNombre(String nombre);

    @Query("select u from Aerolinea u where u.nombre like %?1%")
    List<Aerolinea> encontrarPorNombreQueContenga(String fragmento);

    @Query("select count(u) from Aerolinea u where u.nombre = ?1")
    long contarPorNombre(String nombre);

    @Query("select u from Aerolinea u where u.idAerolinea > ?1 order by u.nombre asc")
    List<Aerolinea> encontrarPorIdAerolineaMayorQueOrdenadoPorNombre(Long id);

    @Query("select u from Aerolinea u order by length(u.nombre) desc limit 1")
    Aerolinea encontrarAerolineaConNombreMasLargo();



}
