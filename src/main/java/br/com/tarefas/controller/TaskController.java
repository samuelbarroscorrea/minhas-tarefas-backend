package br.com.tarefas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import br.com.tarefas.controller.request.TaskFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.TaskModelAssembler;
import br.com.tarefas.controller.request.TaskRequest;
import br.com.tarefas.controller.response.TaskResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.services.TaskService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
public class TaskController {

	@Autowired
	private TaskService tarefaService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TaskModelAssembler assembler;
	
	@GetMapping
	public PagedModel<EntityModel<TaskResponse>> todasTarefas(
	        @ModelAttribute TaskFilter filtro,
	        Pageable pageable
	) {
		System.out.println("-------------Inicio do offset-----------");
		Page<Tarefa> page = tarefaService.buscar(filtro, pageable);

	    List<EntityModel<TaskResponse>> tarefas = page.stream()
	            .map(assembler::toModel)
	            .toList();

	    PagedModel.PageMetadata metadata =
	            new PagedModel.PageMetadata(
	                    page.getSize(),
	                    page.getNumber(),
	                    page.getTotalElements(),
	                    page.getTotalPages()
	            );

	    PagedModel<EntityModel<TaskResponse>> model =
	            PagedModel.of(tarefas, metadata);

	    model.add(WebMvcLinkBuilder.linkTo(
	            WebMvcLinkBuilder.methodOn(TaskController.class)
	                    .todasTarefas(filtro, pageable)
	    ).withSelfRel());
	    
	    System.out.println("-------------Fim do offset-----------");

	    return model;
	}
	
	
	
	
	@GetMapping("/{id}")
	public EntityModel<TaskResponse> umaTarefa(@PathVariable Integer id) {
		Tarefa tarefa = tarefaService.getTarefaPorId(id); 
		return assembler.toModel(tarefa);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<TaskResponse>> salvarTarefa(@Valid @RequestBody TaskRequest tarefaReq) {
		Tarefa tarefa = modelMapper.map(tarefaReq, Tarefa.class);
		Tarefa tarefaSalva = tarefaService.salvarTarefa(tarefa);
		
		EntityModel<TaskResponse> tarefaModel =  assembler.toModel(tarefaSalva);
		
		return ResponseEntity
				.created(tarefaModel
				.getRequiredLink(IanaLinkRelations
				.SELF)
				.toUri()
				)
				.body(tarefaModel);
	}
	
	@DeleteMapping("/{id}")
	public void excluirTarefa(@PathVariable Integer id) {
		tarefaService.deleteById(id);
	}
	
	@PutMapping("/{id}/iniciar")
	public EntityModel<TaskResponse> iniciarTarefa(@PathVariable Integer id) {
		 Tarefa tarefa = tarefaService.iniciarTarefaPorId(id);
		 
		 return assembler.toModel(tarefa);
	}
	
	@PutMapping("/{id}/concluir")
	public EntityModel<TaskResponse> concluirTarefa(@PathVariable Integer id) {
		Tarefa tarefa = tarefaService.concluirTarefaPorId(id);
		return assembler.toModel(tarefa);
	}
	
	@PutMapping("/{id}/cancelar")
	public EntityModel<TaskResponse> cancelarTarefa(@PathVariable Integer id) {
		Tarefa tarefa = tarefaService.cancelarTarefaPorId(id);
		return assembler.toModel(tarefa);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
