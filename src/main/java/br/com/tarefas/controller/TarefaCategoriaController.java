package br.com.tarefas.controller;

import java.util.List;

import br.com.tarefas.controller.request.TarefaCategoriaFiltro;
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

import br.com.tarefas.controller.request.TarefaCategoriaRequest;
import br.com.tarefas.controller.response.TarefaCategoriaResponse;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.services.TarefaCategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
public class TarefaCategoriaController {

    @Autowired
    private TarefaCategoriaService tarefaCategoriaService;

    
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public PagedModel<EntityModel<TarefaCategoriaResponse>> todasCategorias(
            TarefaCategoriaFiltro filtro,
            Pageable pageable) {

        Page<TarefaCategoria> page = tarefaCategoriaService.buscar(filtro, pageable);

        List<EntityModel<TarefaCategoriaResponse>> categorias = page.stream()
                .map(categoria -> modelMapper.map(categoria, TarefaCategoriaResponse.class))
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
    public TarefaCategoriaResponse umaCategoria(@PathVariable Integer id) {
        return modelMapper.map(
                tarefaCategoriaService.getCategoriaPorId(id),
                TarefaCategoriaResponse.class);
    }

    @PostMapping
    public TarefaCategoriaResponse salvarCategoria(
    		@Valid @RequestBody TarefaCategoriaRequest categoriaReq) {

        TarefaCategoria categoria =
                modelMapper.map(categoriaReq, TarefaCategoria.class);

        return modelMapper.map(
                tarefaCategoriaService.salvar(categoria),
                TarefaCategoriaResponse.class);
    }

    @DeleteMapping("/{id}")
    public void excluirTarefa(@PathVariable Integer id) {
        tarefaCategoriaService.deleteById(id);
    }
}