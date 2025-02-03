package com.climedx.inosystem.model;

import com.climedx.inosystem.enums.EstadoCivil;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pacId;

    @NotEmpty(message = "O nome do paciente é obrigatório.")
    @Column(nullable = false, length = 180)
    private String pacNome;

    @NotEmpty(message = "O CPF é obrigatório.")
    @Column(nullable = false, length = 60, unique = true)
    private String pacCpf;

    @Column(length = 60)
    private String pacRg;

    private LocalDate pacNasc;

    @NotEmpty(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    @Column(nullable = false, length = 180, unique = true)
    private String pacEmail;

    @NotNull(message = "O estado civil é obrigatório.")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EstadoCivil pacEstcivil;

    @JsonManagedReference // Evita referência circular
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefone> telefones;

    @JsonManagedReference // Evita referência circular
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;

    @JsonManagedReference
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Caracteristicas caracteristicas;
}
