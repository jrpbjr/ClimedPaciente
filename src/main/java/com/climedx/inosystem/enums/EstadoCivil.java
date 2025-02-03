package com.climedx.inosystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum EstadoCivil {
    SOLTEIRO(1, "Solteiro"),
    CASADO(2, "Casado"),
    DIVORCIADO(3, "Divorciado");

    private final int codigo;
    @Getter
    private final String descricao;

    EstadoCivil(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public int getCodigo() {
        return codigo;
    }


    @JsonCreator
    public static EstadoCivil fromCodigo(int codigo) {
        for (EstadoCivil estado : EstadoCivil.values()) {
            if (estado.getCodigo() == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado civil inválido: " + codigo);
    }
}
