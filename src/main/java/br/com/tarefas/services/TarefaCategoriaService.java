package br.com.tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.tarefas.controller.request.TarefaCategoriaFiltro;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.repository.TarefaCategoriaRepository;
import br.com.tarefas.repository.specification.TarefaCategoriaSpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaCategoriaService {

    @Autowired
    private TarefaCategoriaRepository repositorio;

    public Page<TarefaCategoria> getTodasCategorias(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    public TarefaCategoria getCategoriaPorId(Integer id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public TarefaCategoria salvar(TarefaCategoria categoria) {
        return repositorio.save(categoria);
    }

    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }
    
    public Page<TarefaCategoria> buscar(TarefaCategoriaFiltro filtro, Pageable pageable) {

        Specification<TarefaCategoria> spec =
                TarefaCategoriaSpecification.comFiltro(filtro);

        return repositorio.findAll(spec, pageable);
    }
}