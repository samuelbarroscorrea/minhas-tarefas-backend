package br.com.tarefas.controller;

import java.util.List;

import br.com.tarefas.controller.request.TaskCategoryFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.request.TaskCategoryRequest;
import br.com.tarefas.controller.response.TaskCategoryResponse;
import br.com.tarefas.model.TaskCategory;
import br.com.tarefas.services.TaskCategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
public class TaskCategoryController {

    @Autowired
    private TaskCategoryService tarefaCategoriaService;

    
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public PagedModel<EntityModel<TaskCategoryResponse>> todasCategorias(
            TaskCategoryFilter filtro,
            Pageable pageable) {

        Page<TaskCategory> page = tarefaCategoriaService.buscar(filtro, pageable);

        List<EntityModel<TaskCategoryResponse>> categorias = page.stream()
                .map(categoria -> modelMapper.map(categoria, TaskCategoryResponse.class))
                .map(EntityModel::of)
                .toList();

        PagedModel.PageMetadata metadata =
                new PagedModel.PageMetadata(
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalElements(),
                        page.getTotalPages()
                );

        return PagedModel.of(categorias, metadata);
    }

    @GetMapping("/{id}")
    public TaskCategoryResponse umaCategoria(@PathVariable Integer id) {
        return modelMapper.map(
                tarefaCategoriaService.getCategoriaPorId(id),
                TaskCategoryResponse.class);
    }

    @PostMapping
    public TaskCategoryResponse salvarCategoria(
    		@Valid @RequestBody TaskCategoryRequest categoriaReq) {

        TaskCategory categoria =
                modelMapper.map(categoriaReq, TaskCategory.class);

        return modelMapper.map(
                tarefaCategoriaService.salvar(categoria),
                TaskCategoryResponse.class);
    }

    @DeleteMapping("/{id}")
    public void excluirTarefa(@PathVariable Integer id) {
        tarefaCategoriaService.deleteById(id);
    }
}