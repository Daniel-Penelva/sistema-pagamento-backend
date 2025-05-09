package com.api.sistema_pagamento_backend.entities;

import java.time.LocalDate;

import com.api.sistema_pagamento_backend.enums.PagamentoStatus;
import com.api.sistema_pagamento_backend.enums.TipoPagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder       // permite criar objetos de forma mais legível
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private LocalDate data;
    private double valor;
    private TipoPagamento tipoPagamento;
    private PagamentoStatus pagamentoStatus;
    private String file;

    // Muitos pagamentos podem pertencer a uma único estudante
    @ManyToOne
    private Estudante estudante;
}
