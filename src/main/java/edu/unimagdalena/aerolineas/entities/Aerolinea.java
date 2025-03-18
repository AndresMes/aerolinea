
package edu.unimagdalena.aerolineas.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aerolineas")

public class Aerolinea {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAerolinea;
    @Column(nullable = false)
    private String nombre;

    @ManyToMany
    @JoinTable(
            name = "vuelos_aerolineas",
            joinColumns = @JoinColumn(name = "id_aerolinea", referencedColumnName = "idAerolinea"),
            inverseJoinColumns = @JoinColumn(name = "id_vuelo", referencedColumnName = "idVuelo")
    )
    private Set<Vuelo> vuelos;

    public void addVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }



//    @OneToMany(mappedBy = "aerolinea")
//    private Set<Vuelo> vuelos;

}
