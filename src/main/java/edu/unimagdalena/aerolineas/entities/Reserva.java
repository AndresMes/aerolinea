package edu.unimagdalena.aerolineas.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")

public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReserva;
    @Column(nullable = false)
    private UUID codigoReserva;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_vuelo", referencedColumnName = "idVuelo")
    private Vuelo vuelo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_pasajero", referencedColumnName = "idPasajero")
    private Pasajero pasajero;
}
