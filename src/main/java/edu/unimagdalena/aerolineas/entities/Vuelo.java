package edu.unimagdalena.aerolineas.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vuelos")

public class Vuelo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVuelo;
    @Column(nullable = false, unique = true)
    private UUID numero;
    @Column(nullable = false)
    private String origen;
    @Column(nullable = false)
    private String destino;

    @ManyToMany(mappedBy = "vuelos")
    private Set<Aerolinea> aerolineas;

    @OneToMany(mappedBy = "vuelo")
    private Set<Reserva> reservas;

//    @JoinColumn(name = "id_aerolinea")
//    private Aerolinea aerolinea;

}
