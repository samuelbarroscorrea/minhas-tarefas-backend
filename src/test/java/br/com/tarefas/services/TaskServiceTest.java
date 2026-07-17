package br.com.tarefas.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.tarefas.exception.TaskStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskStatus;
import br.com.tarefas.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
	
	@Mock
	private TaskRepository repositorio;
	
	@InjectMocks
	private TaskService service;
	
	@Test
	void naoDeveConcluirTarefaCancelada() {
		
		Integer idExemplo = 1;
		
		Tarefa tarefa = new Tarefa();
		tarefa.setId(idExemplo);
		tarefa.setDescricao("teste 01");
		tarefa.setStatus(TaskStatus.CANCELADA);
		
		Mockito.when(repositorio.findById(idExemplo)).thenReturn(Optional.of(tarefa));
		
		Assertions.assertThrows(TaskStatusException.class, () -> service.iniciarTarefaPorId(idExemplo));
		
		
		
	}
	
	
	
	
	
	
	
}
