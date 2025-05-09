package com.api.sistema_pagamento_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.sistema_pagamento_backend.entities.Estudante;

@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, Long> {

    // Buscando estudante por código
    Estudante findByCodigo(String codigo);

    // Busca uma lista de estudantes por programa
    List<Estudante> findByProgramaId(String programaId);

}
