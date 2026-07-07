package br.com.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.repository.TarefaCategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaCategoriaService {

    @Autowired
    private TarefaCategoriaRepository repositorio;

    public Page<TarefaCategoria> getTodasCategorias(Pageable pageable) {
        return repositorio.findAll(pageable);
    }
    
    public List<TarefaCategoria> getCategoriasPorCursor(Integer cursor, Pageable pageable) {
    	return repositorio.findByIdGreaterThanOrderByIdAsc(cursor, pageable);
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
}