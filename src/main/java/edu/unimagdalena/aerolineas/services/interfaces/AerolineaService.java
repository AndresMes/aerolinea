package edu.unimagdalena.aerolineas.services.interfaces;

import edu.unimagdalena.aerolineas.entities.Aerolinea;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AerolineaService {

    Aerolinea createAerolinea(Aerolinea aerolinea);
    List<Aerolinea> findByIdAerolinea(Long idAerolinea);
    List<Aerolinea> findByNombreEquals(String nombre);
    List<Aerolinea> findByIdAerolineaBetween(Long idAerolinea1, Long idAerolinea2);
    List<Aerolinea> findByNombreStartingWith(String nombre);
    List<Aerolinea> findByNombreOrderByNombreAsc(String nombre);
    Aerolinea encontrarAerolíneaDistintaPorNombre(String nombre);
    List<Aerolinea> encontrarPorNombreQueContenga(String fragmento);
    long contarPorNombre(String nombre);
    List<Aerolinea> encontrarPorIdAerolineaMayorQueOrdenadoPorNombre(Long id);
    Aerolinea encontrarAerolineaConNombreMasLargo();
    Aerolinea updateAerolinea(Long idAerolinea, Aerolinea aerolineaActualizada);
    void deleteAerolinea(Long idAerolinea);
}
