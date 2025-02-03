package com.climedx.inosystem.model;

import com.climedx.inosystem.enums.TipoTelefone;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "Telefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telId;

    @ManyToOne
    @JoinColumn(name = "pac_id", nullable = false)
    @JsonBackReference // Evita loop infinito ao serializar JSON
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTelefone tipo;

    @Column(nullable = false, length = 45)
    private String numero;
}