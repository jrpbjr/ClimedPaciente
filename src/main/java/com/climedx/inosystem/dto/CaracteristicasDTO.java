package com.climedx.inosystem.dto;

import com.climedx.inosystem.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicasDTO {
    private BigDecimal peso;
    private BigDecimal altura;
    private Genero genero;
}