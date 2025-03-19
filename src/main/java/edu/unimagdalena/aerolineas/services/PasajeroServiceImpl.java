package edu.unimagdalena.aerolineas.services;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.exceptions.PasajeroNotFoundException;
import edu.unimagdalena.aerolineas.repositories.RepositoryPasajero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasajeroServiceImpl implements PasajeroService{
    private final RepositoryPasajero pasajeroRepository;

    @Autowired
    public PasajeroServiceImpl(RepositoryPasajero pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    public Pasajero createPasajero(Pasajero pasajero){
        return pasajeroRepository.save(pasajero);
    }

    public List<Pasajero> findByNombre(String nombre){
        return pasajeroRepository.findByNombre(nombre);
    }

    public List<Pasajero> findByNombreStartingWith(String prefijo){
        return pasajeroRepository.findByNombreStartingWith(prefijo);
    }

    public List<Pasajero> findByNid(String nid){
        return pasajeroRepository.findByNid(nid);
    }

    public List<Pasajero> findByNombreContaining(String fragmento){
        return pasajeroRepository.findByNombreContaining(fragmento);
    }

    public List<Pasajero> findByIdPasajeroGreaterThan(Long id){
        return pasajeroRepository.findByIdPasajeroGreaterThan(id);
    }

    public List<Pasajero> findByReservaId(Long reservaId) {
        return pasajeroRepository.findByReservaId(reservaId);
    }

    public Pasajero encontrarPasajeroConNidMasCorto(){
        return pasajeroRepository.encontrarPasajeroConNidMasCorto();
    }

    public List<Pasajero> encontrarPasajerosConReservas(){
        return pasajeroRepository.encontrarPasajerosConReservas();
    }

    public long contarPorNombre(String nombre){
        return pasajeroRepository.contarPorNombre(nombre);
    }

    public Pasajero encontrarPasajeroConNombreMasLargo(){
        return pasajeroRepository.encontrarPasajeroConNombreMasLargo();
    }

    public Optional<Pasajero> findById(Long id) {
        return pasajeroRepository.findById(id);
    }

    public Pasajero updatePasajero(Long id, Pasajero pasajeroActualizado) {
        return pasajeroRepository.findById(id)
                .map(pasajero -> {
                    pasajero.setNombre(pasajeroActualizado.getNombre());
                    pasajero.setNid(pasajeroActualizado.getNid());
                    return pasajeroRepository.save(pasajero);
                })
                .orElseThrow(() -> new PasajeroNotFoundException(id));
    }

    public void deletePasajero(Long id) {
        if (!pasajeroRepository.existsById(id)) {
            throw new PasajeroNotFoundException(id);
        }
        pasajeroRepository.deleteById(id);
    }
}
