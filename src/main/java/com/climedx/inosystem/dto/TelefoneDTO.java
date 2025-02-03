package com.climedx.inosystem.dto;

import com.climedx.inosystem.enums.TipoTelefone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDTO {
    private TipoTelefone tipo;
    private String numero;
}
