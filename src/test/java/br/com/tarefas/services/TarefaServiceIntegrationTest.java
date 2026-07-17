package br.com.tarefas.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;

@SpringBootTest
public class TarefaServiceIntegrationTest {
	
	@Autowired
	private TarefaService service;
	
	@Test
	void deveIniciarTarefa() {
		Tarefa tarefa = service.iniciarTarefaPorId(1);
		Assertions.assertEquals(TarefaStatus.EM_ANDAMENTO, tarefa.getStatus());
	}
	
	@Test
	void naoDeveIniciarTarefaConcluida() {
		Tarefa tarefa = service.getTarefaPorId(1);
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		service.salvarTarefa(tarefa);
		
		
		Assertions.assertThrows(TarefaStatusException.class, () -> service.iniciarTarefaPorId(1));
	}
	

}
