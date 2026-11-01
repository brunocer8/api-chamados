package com.example.chamados;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    ABERTO, EM_ANDAMENTO, RESOLVIDO;

    @JsonCreator
    public static Status from(String valor) {
        return valueOf(valor.trim().toUpperCase());
    }
}
