package com.climedx.inosystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTRO("Outro");

    private final String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static Genero fromDescricao(String descricao) {
        for (Genero genero : Genero.values()) {
            if (genero.getDescricao().equalsIgnoreCase(descricao)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Gênero inválido: " + descricao);
    }
}
