package br.com.tarefas.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import br.com.tarefas.controller.response.TaskResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskCategory;
import br.com.tarefas.model.TaskStatus;
import br.com.tarefas.model.User;
import br.com.tarefas.services.TaskService;

@SpringBootTest
public class TaskControllerTest {
	
	@Autowired
	private TaskController controller;
	
	@MockitoBean
	private TaskService service;
	
	@Test
	void validaTarefaResponse() {
		int tarefaId = 999;
		
		Tarefa tarefa = new Tarefa();
		tarefa.setId(tarefaId);
		tarefa.setStatus(TaskStatus.ABERTO);
		
		User usuario = new User();
		usuario.setId(1);
		tarefa.setUsuario(usuario);
		
		TaskCategory categoria = new TaskCategory();
		categoria.setId(2);
		tarefa.setCategoria(categoria);
		
		Mockito.when(service.getTarefaPorId(tarefaId)).thenReturn(tarefa);
		
		EntityModel<TaskResponse> tarefaModel = controller.umaTarefa(tarefaId);
		TaskResponse tarefaResp = tarefaModel.getContent();
		
		Assertions.assertEquals(tarefaId, tarefaResp.getId());
		Assertions.assertEquals(2, tarefaResp.getCategoriaId());
		Assertions.assertEquals(1, tarefaResp.getUsuarioId());
		Assertions.assertEquals(TaskStatus.ABERTO.name(), tarefaResp.getStatus());
		
		
	}
	
	

}
