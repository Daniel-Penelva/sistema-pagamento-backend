package com.api.sistema_pagamento_backend.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.sistema_pagamento_backend.entities.Estudante;
import com.api.sistema_pagamento_backend.entities.Pagamento;
import com.api.sistema_pagamento_backend.enums.PagamentoStatus;
import com.api.sistema_pagamento_backend.enums.TipoPagamento;
import com.api.sistema_pagamento_backend.repositories.EstudanteRepository;
import com.api.sistema_pagamento_backend.repositories.PagamentoRepository;
import com.api.sistema_pagamento_backend.services.PagamentoService;


@RestController
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PagamentoService pagamentoService;


    /**
     * Método para criar um novo pagamento.
     * @param file O arquivo do pagamento a ser salvo.
     * @param valor O valor do pagamento.
     * @param tipoPagamento O tipo de pagamento (ex: cartão de crédito, débito, etc.).
     * @param data A data do pagamento.
     * @param codigoEstudante O código do estudante associado ao pagamento.
     * @return O pagamento criado.
     * @throws Exception Se ocorrer algum erro durante o processamento do pagamento.
     */
    @PostMapping(path = "/pagamento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pagamento criarPagamento(@RequestParam("file") MultipartFile file, @RequestParam double valor, @RequestParam TipoPagamento tipoPagamento, @RequestParam LocalDate data, @RequestParam String codigoEstudante) throws Exception {
        return pagamentoService.salvarPagamento(file, valor, tipoPagamento, data, codigoEstudante);
    }

    /**
     * Método para listar todos os estudantes.
     * @return Uma lista de todos os estudantes.
     */
    @GetMapping("/estudantes")
    public List<Estudante> listarEstudantes() {
        List<Estudante> estudantes = estudanteRepository.findAll();
        if (estudantes.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver estudantes
        }
        return estudantes;
    }

    /**
     * Método para buscar um estudante específico pelo código.
     * @param codigo O código do estudante a ser buscado.
     * @return O estudante correspondente ao código fornecido.
     */
    @GetMapping("/estudantes/{codigo}")
    public Estudante buscarEstudantePorCodigo(@PathVariable String codigo) {
        Estudante estudante = estudanteRepository.findByCodigo(codigo);
        if (estudante == null) {
            throw new RuntimeException("Estudante não encontrado com o código: " + codigo);
        }
        return estudante;
    }

    /**
     * Método para listar estudantes por programa.
     * @param programaId O ID do programa a ser filtrado.
     * @return Uma lista de estudantes correspondentes ao programa fornecido.
     */
    @GetMapping("/estudantesPorPrograma")
    public List<Estudante> listarEstudantesPorPrograma(@RequestParam String programaId) {
        List<Estudante> estudantes = estudanteRepository.findByProgramaId(programaId);
        if (estudantes.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver estudantes
        }
        return estudantes;
    }

    /**
     * Método para listar todos os pagamentos.
     * @return Uma lista de todos os pagamentos.
     */
    @GetMapping("/pagamentos")
    public List<Pagamento> listarPagamentos() {
        List<Pagamento> pagamentos = pagamentoRepository.findAll();
        if (pagamentos.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver pagamentos
        }
        return pagamentos;
    }

    /**
     * Método para buscar um pagamento específico pelo ID.
     * @param id O ID do pagamento a ser buscado.
     * @return O pagamento correspondente ao ID fornecido.
     */
    @GetMapping("/pagamentos/{id}")
    public Pagamento buscarPagamentoPorId(@PathVariable Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id).get();
        if (pagamento == null) {
            throw new RuntimeException("Pagamento não encontrado com o ID: " + id);
        }
        return pagamento;
    }

    /**
     * Método para listar pagamentos por código do estudante.
     * @param codigoEstudante O código do estudante a ser filtrado.
     * @return Uma lista de pagamentos correspondentes ao código do estudante fornecido.
     */
    @GetMapping("/estudantes/{codigoEstudante}/pagamentos")
    public List<Pagamento> listarPagamentosPorCodigoEstudante(@PathVariable String codigoEstudante) {
        List<Pagamento> pagamentos = pagamentoRepository.findByEstudanteCodigo(codigoEstudante);
        if (pagamentos.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver pagamentos
        }
        return pagamentos;
    }

    /**
     * Método para listar pagamentos por status.
     * @param pagamentoStatus O status do pagamento a ser filtrado.
     * @return Uma lista de pagamentos correspondentes ao status fornecido.
     */
    @GetMapping("/pagamento/porStatus")
    public List<Pagamento> listarPagamentosPorStatus(@RequestParam PagamentoStatus pagamentoStatus) {
        List<Pagamento> pagamentos = pagamentoRepository.findByPagamentoStatus(pagamentoStatus);
        if (pagamentos.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver pagamentos
        }
        return pagamentos;
    }

    /**
     * Método para atualizar o status de um pagamento específico.
     * @param pagamentoStatus O novo status do pagamento.
     * @param pagamentoId O ID do pagamento a ser atualizado.
     * @return O pagamento atualizado.
     */
    @PutMapping("/pagamento/{pagamentoId}/atualizarPagamento")
    public Pagamento atualizarPagamentoPorStatus(@RequestParam PagamentoStatus pagamentoStatus, @PathVariable Long pagamentoId) {
        Pagamento pagamento = pagamentoService.atualizarPagamentoPorStatus(pagamentoStatus, pagamentoId);
        if (pagamento == null) {
            throw new RuntimeException("Pagamento não encontrado com o ID: " + pagamentoId);
        }
        return pagamento;
    }

    /**
     * Método para listar pagamentos por tipo de pagamento.
     * @param tipoPagamento O tipo de pagamento a ser filtrado.
     * @return Uma lista de pagamentos correspondentes ao tipo de pagamento fornecido.
     */
    @GetMapping("/pagamento/porTipo")
    public List<Pagamento> listarPagamentosPorTipo(@RequestParam TipoPagamento tipoPagamento) {
        List<Pagamento> pagamentos = pagamentoRepository.findByTipoPagamento(tipoPagamento);
        if (pagamentos.isEmpty()) {
            return new ArrayList<>(); // Retorna uma lista vazia se não houver pagamentos
        }
        return pagamentos;
    }

    /**
     * Método para listar o comprovante de pagamento por ID.
     * @param id O ID do pagamento.
     * @return O comprovante de pagamento como um array de bytes.
     * @throws IOException Se ocorrer algum erro ao ler o arquivo.
     */
    @GetMapping(value = "/pagamentoArquivo/{pagamentoId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] listarArquivoPorId(@PathVariable Long pagamentoId) throws IOException {
        return pagamentoService.obterComprovantePagamentoPorId(pagamentoId);
    }
}
