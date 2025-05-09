package com.api.sistema_pagamento_backend.services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.sistema_pagamento_backend.entities.Estudante;
import com.api.sistema_pagamento_backend.entities.Pagamento;
import com.api.sistema_pagamento_backend.enums.PagamentoStatus;
import com.api.sistema_pagamento_backend.enums.TipoPagamento;
import com.api.sistema_pagamento_backend.repositories.EstudanteRepository;
import com.api.sistema_pagamento_backend.repositories.PagamentoRepository;

/* A anotação @Transactional é usada para definir o escopo de uma transação em um método ou classe. Isso significa que todas as operações de banco de dados
 * realizadas dentro desse escopo serão tratadas como uma única unidade de trabalho. Se uma operação falhar, todas as alterações feitas durante a 
 * transação serão revertidas, garantindo a integridade dos dados. */

@Service
@Transactional  // Anotação indica que a classe é um serviço e que as operações dentro dela devem ser tratadas como transações.
public class PagamentoService {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    /**
     * Método para salvar um pagamento.
     * @param file O arquivo do pagamento a ser salvo.
     * @param valor O valor do pagamento.
     * @param tipoPagamento O tipo de pagamento (ex: cartão de crédito, débito, etc.).
     * @param data A data do pagamento.
     * @param codigoEstudante O código do estudante associado ao pagamento.
     * @return O pagamento criado.
     * @throws IOException Se ocorrer algum erro durante o processamento do pagamento.
     */
    public Pagamento salvarPagamento(MultipartFile file, double valor, TipoPagamento tipoPagamento, LocalDate data, String codigoEstudante) throws IOException {

        /* Cria um objeto Path que representa o caminho para a pasta 'sistema_pagamento' que está localizada dentro da pasta 'documentos' do 
         * diretório home do usuáro atual. O método .get() é utilizado para construir um objeto Path a partir de uma sequência de strings que
         * representam partes de um caminho de arquivo ou diretório. O resultado final seria algo como no windows:
         * "C:\Users\NomeDoUsuario\documentos\sistema_pagamento"
         * */
        Path folderPath = Paths.get(System.getProperty("user.home"), "documentos", "sistema_pagamento");

        /* Files é uma classe utilitária que contém métodos para trabalhar com arquivos e diretórios. Aqui, vai verificar se o caminho NÃO existe.
         * E se não existir, ele vai criar todos os diretórios necessários no caminho através do método .createDirectory(). */
        if(!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        /* Cria um identificador Único Universal (UUID) para o arquivo que será salvo. O método toString() vai transformar o UUID em uma string.
         * Aqui, a variável "filename" vai armazenar a representação em string do UUID gerado.*/
        String fileName = UUID.randomUUID().toString();

        /* Obtêm o diretorio home do usuário atual. O método .get() vai construir um objeto Path a partir da sequencia de strings que representa
         * o caminho do diretório. Aqui, vai construir o caminho completo para um arquivo PDF que será salvo na pasta "sistema_pagamento", 
         * dentro da pasta "documentos" do diretório home do usuário. O nome do arquivo é gerado a partir de um UUID aleatório, garantindo que o 
         * nome do arquivo seja único, e a extensão .pdf é adicionada para indicar que se trata de um arquivo PDF.
        */
        Path filePath = Paths.get(System.getProperty("user.home"),"documentos", "sistema_pagamento", fileName+".pdf");

        /* Aqui, o método .copy() vai ser utilizado para copiar os dados de um fluxo de entrada  (InputStream) para um arquivo ou diretório 
         * especificado pelo objeto Path (neste caso, filePath). A instância "file" vai lidar com os uploads de arquivos. O método .getInputStream()
         * vai obter o fluxo de entrada (InputStream) que permite ler os dados do arquivo que foi enviado (ou seja, o arquivo que o usuário fez 
         * upload). */
        Files.copy(file.getInputStream(), filePath);

        Estudante estudante = estudanteRepository.findByCodigo(codigoEstudante);
        if (estudante == null) {
            throw new RuntimeException("Estudante não encontrado com o código: " + codigoEstudante);
        }

        Pagamento pagamento = Pagamento.builder()
                .estudante(estudante)
                .valor(valor)
                .tipoPagamento(tipoPagamento)
                .pagamentoStatus(PagamentoStatus.CRIADO)
                .data(data)
                .file(filePath.toUri().toString())
                .build();

        return pagamentoRepository.save(pagamento);
        
    }


    /**
     * Método para obter o comprovante de pagamento por ID.
     * @param pagamentoId O ID do pagamento.
     * @return O comprovante de pagamento como um array de bytes.
     * @throws IOException Se ocorrer algum erro ao ler o arquivo.
     */
    public byte[] obterComprovantePagamentoPorId(Long pagamentoId) throws IOException {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com o ID: " + pagamentoId));
        
        /* Aqui, obtêm o caminho do arquivo associado a um pagamento, converte esse caminho de string para um URI, e então cria um objeto "Path" 
         * a partir desse URI. Isso é útil para enviar o arquivo como resposta em uma requisição HTTP, por exemplo, quando um usuário solicita o 
         * comprovante de pagamento. 
         * 
         * O método .readAllBytes() vai ler todos os bytes do arquivo e retornar como um array de bytes. 
         * O método Path.of(URI uri) vai criar um objeto Path a partir do URI fornecido. 
         * O método URI.create(String str) é utilizado para criar uma instância de URI a partir da string fornecida. Um URI é uma forma padronizada 
         * de identificar um recurso, que pode ser um arquivo local, um recurso na web, etc.
         * O método .getFile() vai lê e retornar o caminho do arquivo associado ao pagamento.
         */
        return Files.readAllBytes(Path.of(URI.create(pagamento.getFile())));
    }


    /**
     * Método para atualizar o status do pagamento.
     * @param pagamentoStatus O novo status do pagamento.
     * @param pagamentoId O ID do pagamento a ser atualizado.
     * @return O pagamento atualizado.
     */
    public Pagamento atualizarPagamentoPorStatus(PagamentoStatus pagamentoStatus, Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com o ID: " + pagamentoId));

        // Atualiza o status do pagamento com valor recebido como parâmetro e salva o pagamento atualizado no repositório.
        pagamento.setPagamentoStatus(pagamentoStatus);
        return pagamentoRepository.save(pagamento);
    }
    
}
