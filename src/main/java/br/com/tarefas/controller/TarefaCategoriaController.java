package br.com.tarefas.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.TarefaCategoriaModelAssembler;
import br.com.tarefas.controller.request.TarefaCategoriaRequest;
import br.com.tarefas.controller.response.TarefaCategoriaResponse;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.services.TarefaCategoriaService;

@RestController
public class TarefaCategoriaController {

    @Autowired
    private TarefaCategoriaService service;

    @Autowired
    private TarefaCategoriaModelAssembler assembler;
    
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/categoria")
    public PagedModel<EntityModel<TarefaCategoriaResponse>> todasCategorias(Pageable pageable) {

        Page<TarefaCategoria> page = service.getTodasCategorias(pageable);

        List<EntityModel<TarefaCategoriaResponse>> categorias = page.stream()
                .map(categoria -> mapper.map(categoria, TarefaCategoriaResponse.class))
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
    
    @GetMapping("/categoria/cursor")
    public CollectionModel<EntityModel<TarefaCategoriaResponse>> categoriaPorCursor(
    		@RequestParam(required = false) Integer cursor,
    		@RequestParam(defaultValue = "5") Integer size) {
    	
    	Pageable pageable = PageRequest.of(0, size);

        if (cursor == null) {
            cursor = 0;
        }
        
        List<TarefaCategoria> categorias = service.getCategoriasPorCursor(cursor, pageable);

        List<EntityModel<TarefaCategoriaResponse>> categoriasModel = categorias.stream()
                .map(assembler::toModel)
                .toList();
        
        CollectionModel<EntityModel<TarefaCategoriaResponse>> model =
                CollectionModel.of(
                        categoriasModel,
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(TarefaCategoriaController.class)
                                        .categoriaPorCursor(cursor, size)
                        ).withSelfRel()
                );

        return model;
    }

    @GetMapping("/categoria/{id}")
    public TarefaCategoriaResponse umaCategoria(@PathVariable Integer id) {
        return mapper.map(
                service.getCategoriaPorId(id),
                TarefaCategoriaResponse.class);
    }

    @PostMapping("/categoria")
    public TarefaCategoriaResponse salvarCategoria(
            @RequestBody TarefaCategoriaRequest categoriaReq) {

        TarefaCategoria categoria =
                mapper.map(categoriaReq, TarefaCategoria.class);

        return mapper.map(
                service.salvar(categoria),
                TarefaCategoriaResponse.class);
    }

    @DeleteMapping("/categoria/{id}")
    public void excluirTarefa(@PathVariable Integer id) {
        service.deleteById(id);
    }
}