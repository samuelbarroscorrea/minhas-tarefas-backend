package br.com.tarefas.controller.assembler;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.tarefas.controller.TaskCategoryController;
import br.com.tarefas.controller.TaskController;
import br.com.tarefas.controller.UserController;
import br.com.tarefas.controller.response.TaskResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskStatus;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Tarefa, EntityModel<TaskResponse>> {

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public EntityModel<TaskResponse> toModel(Tarefa tarefa) {
		
		TaskResponse tarefaResp = mapper.map(tarefa, TaskResponse.class);
		
		EntityModel<TaskResponse> tarefaModel = EntityModel.of(tarefaResp,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class).umaTarefa(tarefaResp.getId())).withSelfRel(),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class)
					    .todasTarefas(null, Pageable.unpaged())).withRel("tarefas"),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskCategoryController.class).umaCategoria(tarefaResp.getCategoriaId())).withRel("categoria"),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).umUsuario(tarefaResp.getUsuarioId())).withRel("usuario"));
		
		if(TaskStatus.EM_ANDAMENTO.equals(tarefa.getStatus())) {
			tarefaModel.add(
					WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class).concluirTarefa(tarefa.getId())).withRel("concluir"),
					WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class).cancelarTarefa(tarefa.getId())).withRel("cancelar")
					);
		}
		
		if(TaskStatus.ABERTO.equals(tarefa.getStatus())) {
			tarefaModel.add(
					WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaskController.class).iniciarTarefa(tarefa.getId())).withRel("iniciar")
					);
		}
		
		return tarefaModel;
	}

}
