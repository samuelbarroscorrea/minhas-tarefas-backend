package br.com.tarefas.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tarefas.exception.TaskStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskStatus;

@SpringBootTest
public class TarefaServiceIntegrationTest {
	
	@Autowired
	private TaskService service;
	
	@Test
	void deveIniciarTarefa() {
		Tarefa tarefa = service.iniciarTarefaPorId(1);
		Assertions.assertEquals(TaskStatus.EM_ANDAMENTO, tarefa.getStatus());
	}
	
	@Test
	void naoDeveIniciarTarefaConcluida() {
		Tarefa tarefa = service.getTarefaPorId(1);
		tarefa.setStatus(TaskStatus.CONCLUIDA);
		service.salvarTarefa(tarefa);
		
		
		Assertions.assertThrows(TaskStatusException.class, () -> service.iniciarTarefaPorId(1));
	}
	

}
