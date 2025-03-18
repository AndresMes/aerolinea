package edu.unimagdalena.aerolineas.entities;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pasaportes")
public class Pasaporte {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPasaporte;

    @Column(nullable = false)
    private String numero;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pasajero",referencedColumnName = "idPasajero")
    private Pasajero pasajero;

}
