package com.climedx.inosystem.model;

import com.climedx.inosystem.enums.EstadoCivil;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
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

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Column(nullable = false)
    private LocalDate pacNasc; // Data de nascimento

    @Column(nullable = false)
    private Integer pacIdade; // Idade calculada

    @Column(nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private boolean pacInfantil;  // Se true (1), é menor de 16 anos; caso contrário, false (0)

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

    // Método para calcular a idade
    public void calcularIdade() {
        if (this.pacNasc != null) {
            this.pacIdade = Period.between(this.pacNasc, LocalDate.now()).getYears();
            this.pacInfantil = this.pacIdade <= 16;
        }
    }

    // Garante que a idade seja calculada antes de salvar ou atualizar
    @PrePersist
    @PreUpdate
    private void preSalvar() {
        calcularIdade();
    }
}
