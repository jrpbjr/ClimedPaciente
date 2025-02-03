package com.climedx.inosystem.dto;

import com.climedx.inosystem.enums.EstadoCivil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private Long pacId;
    private String pacNome;
    private String pacCpf;
    private String pacEmail;
    private EstadoCivil pacEstcivil;
    private LocalDate pacNasc; // Data de nascimento
    private Integer pacIdade;  // Idade calculada
    private List<TelefoneDTO> telefones;
    private List<EnderecoDTO> enderecos;
    private CaracteristicasDTO caracteristicas;
}
