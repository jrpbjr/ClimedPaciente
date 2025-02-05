package com.climedx.inosystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long end_id;

    @ManyToOne
    @JoinColumn(name = "pac_id", nullable = false)
    @JsonBackReference // Evita loop infinito ao serializar JSON
    private Paciente paciente;

    @Column(nullable = false, length = 150)
    private String rua;

    @Column(nullable = false, length = 120)
    private String bairro;

    @Column(nullable = false, length = 120)
    private String cidade;

    @Column(nullable = false, length = 6)
    private String estado;

    @Column(nullable = false, length = 27)
    private String cep;
}