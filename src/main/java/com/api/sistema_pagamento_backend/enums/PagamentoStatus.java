package com.api.sistema_pagamento_backend.enums;

public enum PagamentoStatus {
    CRIADO, VALIDADO, RECUSADO;

    // CRIADO: O pagamento foi criado, mas ainda não foi validado.
    // VALIDADO: O pagamento foi validado e está em processamento.
    // RECUSADO: O pagamento foi recusado por algum motivo (ex: saldo insuficiente, dados inválidos, etc.).
}
