package com.climedx.inosystem.model;

import com.climedx.inosystem.enums.Genero;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Caracteristicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Caracteristicas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @OneToOne
    @JoinColumn(name = "pac_id", nullable = false)
    @JsonBackReference // Evita referência circular
    private Paciente paciente;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal altura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;
}
