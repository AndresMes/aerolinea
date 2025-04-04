package edu.unimagdalena.aerolineas.services.impl;

import edu.unimagdalena.aerolineas.entities.Vuelo;
import edu.unimagdalena.aerolineas.exceptions.VueloNotFoundException;
import edu.unimagdalena.aerolineas.repositories.RepositoryVuelo;
import edu.unimagdalena.aerolineas.services.interfaces.VueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VueloServiceImpl implements VueloService {

    private final RepositoryVuelo vueloRepository;

    @Autowired
    public VueloServiceImpl(RepositoryVuelo vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    @Override
    public Vuelo crearVuelo(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    @Override
    public List<Vuelo> buscarTodosVuelos() {
        return vueloRepository.findAll();
    }

    @Override
    public Optional<Vuelo> buscarPorId(Long id) {
        return vueloRepository.findById(id);
    }

    @Override
    public List<Vuelo> buscarPorOrigen(String origen) {
        return vueloRepository.findByOrigen(origen);
    }

    @Override
    public List<Vuelo> buscarPorDestino(String destino) {
        return vueloRepository.findByDestino(destino);
    }

    @Override
    public List<Vuelo> buscarPorOrigenYDestino(String origen, String destino) {
        return vueloRepository.findByOrigenAndDestino(origen, destino);
    }

    @Override
    public List<Vuelo> buscarVuelosConIdMayorA(Long idVuelo) {
        return vueloRepository.findByIdVueloGreaterThan(idVuelo);
    }

    @Override
    public List<Vuelo> buscarDestinosQueEmpiecenCon(String prefijo) {
        return vueloRepository.findByDestinoStartingWith(prefijo);
    }

    @Override
    public List<Vuelo> buscarVuelosNumeroTerminaEn(String sufijo) {
        return vueloRepository.encontrarVuelosPorNumeroTerminandoEn(sufijo);
    }

    @Override
    public Vuelo encontrarVueloConDestinoMasLargo() {
        return vueloRepository.encontrarVueloConDestinoMasLargo();
    }

    @Override
    public List<Vuelo> buscarPorFragmentoNumero(String fragmento) {
        return vueloRepository.encontrarPorNumeroQueContenga(fragmento);
    }

    @Override
    public long contarVuelosPorOrigen(String origen) {
        return vueloRepository.contarPorOrigen(origen);
    }

    @Override
    public Vuelo encontrarVueloConOrigenMasLargo() {
        return vueloRepository.encontrarVueloConOrigenMasLargo();
    }

    @Override
    public Vuelo actualizarVuelo(Long id, Vuelo vueloActualizado) {
        return vueloRepository.findById(id)
                .map(vuelo -> {
                    vuelo.setNumero(vueloActualizado.getNumero());
                    vuelo.setOrigen(vueloActualizado.getOrigen());
                    vuelo.setDestino(vueloActualizado.getDestino());
                    vuelo.setAerolineas(vueloActualizado.getAerolineas());
                    vuelo.setReservas(vueloActualizado.getReservas());
                    return vueloRepository.save(vuelo);
                })
                .orElseThrow(() -> new VueloNotFoundException(id));
    }

    @Override
    public void eliminarVuelo(Long id) {
        if (!vueloRepository.existsById(id)) {
            throw new VueloNotFoundException(id);
        }
        vueloRepository.deleteById(id);
    }
}