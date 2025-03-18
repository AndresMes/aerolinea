package edu.unimagdalena.aerolineas.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pasajeros")
public class Pasajero {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPasajero;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String nid;

    @OneToOne(mappedBy = "pasajero", cascade = CascadeType.ALL)
    private Pasaporte pasaporte;

    @Builder.Default // ← Esto asegura que la lista se inicialice
    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();
}
