package edu.unimagdalena.aerolineas.repositories;

import edu.unimagdalena.aerolineas.entities.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryPasaporte extends JpaRepository<Pasaporte, Long>{

    Pasaporte findByNumero(String numero);
    List<Pasaporte> findByNumeroBetween(String numero1, String numero2);
    List<Pasaporte> findByIdPasaporteBefore(Long idPasaporteBefore);
    List<Pasaporte> findByNumeroAfter(String numeroAfter);
    List<Pasaporte> findByNumeroBetweenAndNumeroAfter(String numero1, String numero2, String numero3);

    @Query("SELECT p FROM Pasaporte p WHERE CAST(p.idPasaporte AS string) LIKE '2%'")
    List<Pasaporte> buscarPasaportesComenzarEnDos();

    @Query("select p from Pasaporte as p")
    List<Pasaporte> buscarTodosPasaportes();

    @Query("select p from Pasaporte as p where p.numero between ?1 and ?2")
    List<Pasaporte> buscarPasaporteRango(String numero1, String numero2);

    @Query("select p from Pasaporte as p where p.idPasaporte > 4")
    List<Pasaporte> buscarPasaporteMayoresCuatro();

    @Query("select p from Pasaporte as p where p.idPasaporte between 1 and 100")
    List<Pasaporte> buscarPasaporteEntreUnoYCien();


}
