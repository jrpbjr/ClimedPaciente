package com.climedx.inosystem.dto;

import com.climedx.inosystem.enums.EstadoCivil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<TelefoneDTO> telefones;
    private List<EnderecoDTO> enderecos;
    private CaracteristicasDTO caracteristicas;
}
