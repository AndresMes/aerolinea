package edu.unimagdalena.aerolineas.services;

import edu.unimagdalena.aerolineas.entities.Pasajero;
import edu.unimagdalena.aerolineas.repositories.RepositoryPasajero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;


//Declaramos que es servicio
@Service
public class PasajeroService {


    //Declaramos un atributo que corresponde al repositorio de la clase del servicio
    private final RepositoryPasajero pasajeroRepository;

    @Autowired
    //Constructor
    public PasajeroService(RepositoryPasajero pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    //Definimos todos los métodos y lógica de negocio en general


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

    public List<Pasajero> encontrarPasajerosConReservasPorNombre(@Param("nombre") String nombre){
        return pasajeroRepository.encontrarPasajerosConReservasPorNombre(nombre);
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


}
