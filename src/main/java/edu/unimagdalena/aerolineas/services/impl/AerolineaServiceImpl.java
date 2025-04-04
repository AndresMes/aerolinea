package edu.unimagdalena.aerolineas.services.impl;

import edu.unimagdalena.aerolineas.entities.Aerolinea;
import edu.unimagdalena.aerolineas.exceptions.AerolineaNotFoundException;
import edu.unimagdalena.aerolineas.repositories.RepositoryAerolinea;
import edu.unimagdalena.aerolineas.services.interfaces.AerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AerolineaServiceImpl implements AerolineaService {

    private final RepositoryAerolinea aerolineaRepository;

    @Autowired
    public AerolineaServiceImpl(RepositoryAerolinea aerolineaRepository) {
        this.aerolineaRepository = aerolineaRepository;
    }

    @Override
    public Aerolinea createAerolinea(Aerolinea aerolinea) {
        return aerolineaRepository.save(aerolinea);
    }

    @Override
    public List<Aerolinea> findByIdAerolinea(Long idAerolinea) {
        return aerolineaRepository.findByIdAerolinea(idAerolinea);
    }

    @Override
    public List<Aerolinea> findByNombreEquals(String nombre) {
        return aerolineaRepository.findByNombreEquals(nombre);
    }

    @Override
    public List<Aerolinea> findByIdAerolineaBetween(Long idAerolinea1, Long idAerolinea2) {
        return aerolineaRepository.findByIdAerolineaBetween(idAerolinea1, idAerolinea2);
    }

    @Override
    public List<Aerolinea> findByNombreStartingWith(String nombre) {
        return aerolineaRepository.findByNombreStartingWith(nombre);
    }

    @Override
    public List<Aerolinea> findByNombreOrderByNombreAsc(String nombre) {
        return aerolineaRepository.findByNombreOrderByNombreAsc(nombre);
    }

    @Override
    public Aerolinea encontrarAerolíneaDistintaPorNombre(String nombre) {
        return aerolineaRepository.encontrarAerolíneaDistintaPorNombre(nombre);
    }

    @Override
    public List<Aerolinea> encontrarPorNombreQueContenga(String fragmento) {
        return aerolineaRepository.encontrarPorNombreQueContenga(fragmento);
    }

    @Override
    public long contarPorNombre(String nombre) {
        return aerolineaRepository.contarPorNombre(nombre);
    }

    @Override
    public List<Aerolinea> encontrarPorIdAerolineaMayorQueOrdenadoPorNombre(Long id) {
        return aerolineaRepository.encontrarPorIdAerolineaMayorQueOrdenadoPorNombre(id);
    }

    @Override
    public Aerolinea encontrarAerolineaConNombreMasLargo() {
        return aerolineaRepository.encontrarAerolineaConNombreMasLargo();
    }


    public Optional<Aerolinea> findById(Long id){
        return aerolineaRepository.findById(id);
    }

    @Override
    public Aerolinea updateAerolinea(Long idAerolinea, Aerolinea aerolineaActualizada) {
        return aerolineaRepository.findById(idAerolinea)
                .map(aerolinea -> {
                    aerolinea.setNombre(aerolineaActualizada.getNombre());
                    aerolinea.setVuelos(aerolineaActualizada.getVuelos());
                    return aerolineaRepository.save(aerolinea);
                })
                .orElseThrow(() -> new AerolineaNotFoundException(idAerolinea));
    }

    @Override
    public void deleteAerolinea(Long idAerolinea) {
        if(!aerolineaRepository.existsById(idAerolinea)){
            throw new AerolineaNotFoundException(idAerolinea);
        }
        aerolineaRepository.deleteById(idAerolinea);
    }
}
