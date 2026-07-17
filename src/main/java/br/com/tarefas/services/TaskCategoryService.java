package br.com.tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.tarefas.controller.request.TaskCategoryFilter;
import br.com.tarefas.model.TaskCategory;
import br.com.tarefas.repository.TaskCategoryRepository;
import br.com.tarefas.repository.specification.TaskCategorySpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskCategoryService {

    @Autowired
    private TaskCategoryRepository repositorio;

    public Page<TaskCategory> getTodasCategorias(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    public TaskCategory getCategoriaPorId(Integer id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public TaskCategory salvar(TaskCategory categoria) {
        return repositorio.save(categoria);
    }

    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }
    
    public Page<TaskCategory> buscar(TaskCategoryFilter filtro, Pageable pageable) {

        Specification<TaskCategory> spec =
                TaskCategorySpecification.comFiltro(filtro);

        return repositorio.findAll(spec, pageable);
    }
}