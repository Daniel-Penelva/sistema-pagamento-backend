package com.api.sistema_pagamento_backend.dtos;

import java.time.LocalDate;

import com.api.sistema_pagamento_backend.enums.TipoPagamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPagamentoDto {

    private double valor;
    private TipoPagamento tipoPagamento;
    private LocalDate data;
    private Long codigoEstudante;
    
}
