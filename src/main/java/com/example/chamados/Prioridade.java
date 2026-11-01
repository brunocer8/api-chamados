package com.example.chamados;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Prioridade {
    BAIXA, MEDIA, ALTA;

    /** Aceita "alta", "ALTA", " Alta " etc. Lança IllegalArgumentException se inválido. */
    @JsonCreator
    public static Prioridade from(String valor) {
        return valueOf(valor.trim().toUpperCase());
    }
}
