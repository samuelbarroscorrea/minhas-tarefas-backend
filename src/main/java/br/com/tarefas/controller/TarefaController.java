package br.com.tarefas.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.TarefaModelAssembler;
import br.com.tarefas.controller.request.TarefaRequest;
import br.com.tarefas.controller.response.TarefaResponse;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.services.TarefaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

	@Autowired
	private TarefaService service;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TarefaModelAssembler assembler;
	
	@GetMapping
	public PagedModel<EntityModel<TarefaResponse>> todasTarefas(
	        @RequestParam(required = false) String descricao,
	        Pageable pageable
	) {
		System.out.println("-------------Inicio do offset-----------");

	    Page<Tarefa> page;
	    //TODO Refatorar para ter um único método de paginação, adicionar outros filtro, ordenar a paginação, revisar a api: semantica
	    if (descricao == null) {
	        page = service.getTodasTarefas(pageable);
	    } else {
	        page = service.getTarefasPorDescricao(descricao, pageable);
	    }

	    List<EntityModel<TarefaResponse>> tarefas = page.stream()
	            .map(assembler::toModel)
	            .toList();

	    PagedModel.PageMetadata metadata =
	            new PagedModel.PageMetadata(
	                    page.getSize(),
	                    page.getNumber(),
	                    page.getTotalElements(),
	                    page.getTotalPages()
	            );

	    PagedModel<EntityModel<TarefaResponse>> model =
	            PagedModel.of(tarefas, metadata);

	    model.add(WebMvcLinkBuilder.linkTo(
	            WebMvcLinkBuilder.methodOn(TarefaController.class)
	                    .todasTarefas(descricao, pageable)
	    ).withSelfRel());
	    
	    System.out.println("-------------Fim do offset-----------");

	    return model;
	}
	
	@GetMapping("/cursor")
	public CollectionModel<EntityModel<TarefaResponse>> tarefasPorCursor(
			@RequestParam(required = false) Integer cursor,
			@RequestParam(defaultValue = "5") Integer size) {
		
		Pageable pageable = PageRequest.of(0,  size);
		
		if(cursor == null) {
			cursor = 0;
		}
		
		List<Tarefa> tarefas = service.getTarefasPorCursor(cursor, pageable);
		List<EntityModel<TarefaResponse>> tarefasModel = tarefas.stream().map(assembler::toModel).toList();
		
		CollectionModel<EntityModel<TarefaResponse>> model =
		        CollectionModel.of(
		                tarefasModel,
		                WebMvcLinkBuilder.linkTo(
		                        WebMvcLinkBuilder.methodOn(TarefaController.class)
		                                .tarefasPorCursor(cursor, size))
		                        .withSelfRel()
		        );
		return model;
	}
	
	@GetMapping("/{id}")
	public EntityModel<TarefaResponse> umaTarefa(@PathVariable Integer id) {
		Tarefa tarefa = service.getTarefaPorId(id); 
		return assembler.toModel(tarefa);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<TarefaResponse>> salvarTarefa(@Valid @RequestBody TarefaRequest tarefaReq) {
		Tarefa tarefa = mapper.map(tarefaReq, Tarefa.class);
		Tarefa tarefaSalva = service.salvarTarefa(tarefa);
		
		EntityModel<TarefaResponse> tarefaModel =  assembler.toModel(tarefaSalva);
		
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
		service.deleteById(id);
	}
	
	@PutMapping("/{id}/iniciar")
	public EntityModel<TarefaResponse> iniciarTarefa(@PathVariable Integer id) {
		 Tarefa tarefa = service.iniciarTarefaPorId(id);
		 
		 return assembler.toModel(tarefa);
	}
	
	@PutMapping("/{id}/concluir")
	public EntityModel<TarefaResponse> concluirTarefa(@PathVariable Integer id) {
		Tarefa tarefa = service.concluirTarefaPorId(id);
		return assembler.toModel(tarefa);
	}
	
	@PutMapping("/{id}/cancelar")
	public EntityModel<TarefaResponse> cancelarTarefa(@PathVariable Integer id) {
		Tarefa tarefa = service.cancelarTarefaPorId(id);
		return assembler.toModel(tarefa);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
