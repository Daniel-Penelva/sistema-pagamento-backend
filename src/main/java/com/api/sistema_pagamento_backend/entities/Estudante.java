package com.api.sistema_pagamento_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder       // permite criar objetos de forma mais legível
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String nome;
    private String sobrenome;

    @Column(unique = true) // garante que o código seja único
    private String codigo;
    private String programaId;
    private String foto;

}
