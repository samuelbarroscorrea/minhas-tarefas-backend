package br.com.tarefas.controller.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.tarefas.controller.TaskCategoryController;
import br.com.tarefas.controller.request.TaskCategoryFilter;
import br.com.tarefas.controller.response.TaskCategoryResponse;
import br.com.tarefas.model.TaskCategory;

@Component
public class TaskCategoryModelAssembler implements RepresentationModelAssembler<TaskCategory, EntityModel<TaskCategoryResponse>> {
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public EntityModel<TaskCategoryResponse> toModel(TaskCategory categoria) {
		TaskCategoryResponse categoriaResp = mapper.map(categoria, TaskCategoryResponse.class);
		
		
		EntityModel<TaskCategoryResponse> tarefaModel = EntityModel.of(
		        categoriaResp,
		        WebMvcLinkBuilder.linkTo(
		                WebMvcLinkBuilder.methodOn(TaskCategoryController.class)
		                        .umaCategoria(categoriaResp.getId()))
		                .withSelfRel(),
		        WebMvcLinkBuilder.linkTo(
		                WebMvcLinkBuilder.methodOn(TaskCategoryController.class)
		                        .todasCategorias(new TaskCategoryFilter() ,Pageable.unpaged()))
		                .withRel("categorias"));

		
		
		return tarefaModel;
	}
	
	

}
