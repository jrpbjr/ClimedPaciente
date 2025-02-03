package com.climedx.inosystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTelefone {
    CELULAR("Celular"),
    RESIDENCIAL("Residencial"),
    OUTRO("Outro");

    private final String descricao;

    TipoTelefone(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static TipoTelefone fromDescricao(String descricao) {
        for (TipoTelefone tipo : TipoTelefone.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de telefone inválido: " + descricao);
    }
}
