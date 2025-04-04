package edu.unimagdalena.aerolineas.services.interfaces;

import edu.unimagdalena.aerolineas.entities.Vuelo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface VueloService {
    Vuelo crearVuelo(Vuelo vuelo);
    List<Vuelo> buscarTodosVuelos();
    Optional<Vuelo> buscarPorId(Long id);
    List<Vuelo> buscarPorOrigen(String origen);
    List<Vuelo> buscarPorDestino(String destino);
    List<Vuelo> buscarPorOrigenYDestino(String origen, String destino);
    List<Vuelo> buscarVuelosConIdMayorA(Long idVuelo);
    List<Vuelo> buscarDestinosQueEmpiecenCon(String prefijo);
    List<Vuelo> buscarVuelosNumeroTerminaEn(String sufijo);
    Vuelo encontrarVueloConDestinoMasLargo();
    List<Vuelo> buscarPorFragmentoNumero(String fragmento);
    long contarVuelosPorOrigen(String origen);
    Vuelo encontrarVueloConOrigenMasLargo();
    Vuelo actualizarVuelo(Long id, Vuelo vueloActualizado);
    void eliminarVuelo(Long id);
}