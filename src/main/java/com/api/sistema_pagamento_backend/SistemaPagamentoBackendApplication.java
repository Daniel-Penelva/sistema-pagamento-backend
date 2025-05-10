package com.api.sistema_pagamento_backend;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.api.sistema_pagamento_backend.entities.Estudante;
import com.api.sistema_pagamento_backend.entities.Pagamento;
import com.api.sistema_pagamento_backend.enums.PagamentoStatus;
import com.api.sistema_pagamento_backend.enums.TipoPagamento;
import com.api.sistema_pagamento_backend.repositories.EstudanteRepository;
import com.api.sistema_pagamento_backend.repositories.PagamentoRepository;

@SpringBootApplication
public class SistemaPagamentoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaPagamentoBackendApplication.class, args);
	}

	@Profile("dev") // O método só será executado se o perfil ativo for "dev"
	@Bean           // Significa que o método retorna um objeto que deve ser gerenciado pelo Spring
	CommandLineRunner commandLineRunner(EstudanteRepository estudanteRepository, PagamentoRepository pagamentoRepository) {
		return args -> {
			estudanteRepository.save(Estudante.builder()
					.nome("Vanessa")
					.sobrenome("Mota")
					.codigo("4567")
					.programaId("LTA1")
					.build());

			estudanteRepository.save(Estudante.builder()
					.nome("Talita")
					.sobrenome("Cunha")
					.codigo("51234")
					.programaId("LTA1")
					.build());

			estudanteRepository.save(Estudante.builder()
					.nome("Rafael")
					.sobrenome("Nunes")
					.codigo("124990")
					.programaId("LTA1")
					.build());

			// Gerando Pagamentos Aleatórios
			TipoPagamento tipoPagamento[] = TipoPagamento.values();  // Pegando os valores do enum TipoPagamento
			Random random = new Random();                            // Criando um objeto Random para gerar números aleatórios
			
			// Criando 10 pagamentos para cada estudante
			estudanteRepository.findAll().forEach(estudante -> {
				for (int i=0; i<10; i++) {
					int index = random.nextInt(tipoPagamento.length);  // Gerando um índice aleatório para o array de tipos de pagamento
					Pagamento pagamento = Pagamento.builder()
							.valor(1000 + (int) (Math.random() * 20000)) // Gerando um valor aleatório entre 1000 e 21000
							.tipoPagamento(tipoPagamento[index])
							.pagamentoStatus(PagamentoStatus.CRIADO)
							.data(LocalDate.now())
							.estudante(estudante)
							.build();						

					pagamentoRepository.save(pagamento);
				}
			});
		};
	}

}
