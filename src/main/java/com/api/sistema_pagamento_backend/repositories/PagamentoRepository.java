package com.api.sistema_pagamento_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.sistema_pagamento_backend.entities.Pagamento;
import com.api.sistema_pagamento_backend.enums.PagamentoStatus;
import com.api.sistema_pagamento_backend.enums.TipoPagamento;



@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{

    // Busca uma lista de pagamentos dos estudantes pelo código
    List<Pagamento> findByEstudanteCodigo(String codigo);

    // Busca uma lista de pagamentos dos estudantes pelo status do pagamento
    List<Pagamento> findByPagamentoStatus(PagamentoStatus pagamentoStatus);

    // Busca uma lista de pagamentos dos estudantes pelo tipo do pagamento
    List<Pagamento> findByTipoPagamento(TipoPagamento tipoPagamento);
    
}
