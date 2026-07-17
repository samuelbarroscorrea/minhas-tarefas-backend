package br.com.tarefas.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.tarefas.controller.request.TaskFilter;
import br.com.tarefas.exception.TaskStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskStatus;
import br.com.tarefas.repository.TaskRepository;
import br.com.tarefas.repository.specification.TaskSpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository repositorio;
	
	public Page<Tarefa> buscar(TaskFilter filtro, Pageable pageable) {

	    Specification<Tarefa> specification =
	            TaskSpecification.comFiltro(filtro);

	    return repositorio.findAll(specification, pageable);
	}
	
	public Tarefa getTarefaPorId(Integer id) {
		return repositorio.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public Tarefa salvarTarefa(Tarefa tarefa) {
		return repositorio.save(tarefa);
	}
	
	public void deleteById(Integer id) {
		repositorio.deleteById(id);
	}
	
	public Tarefa iniciarTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if (!TaskStatus.ABERTO.equals(tarefa.getStatus())) {
			throw new TaskStatusException();
		}
		 
		tarefa.setStatus(TaskStatus.EM_ANDAMENTO);
		repositorio.save(tarefa);
		return tarefa;
		
	}
	
	public Tarefa concluirTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if (TaskStatus.CANCELADA.equals(tarefa.getStatus())) {
			throw new TaskStatusException();
		}
		 
		tarefa.setStatus(TaskStatus.CONCLUIDA);
		repositorio.save(tarefa);
		return tarefa;
		
	}
	
	public Tarefa cancelarTarefaPorId(Integer id) {
	    Tarefa tarefa = getTarefaPorId(id);

	    if (TaskStatus.CONCLUIDA.equals(tarefa.getStatus())) {
	        throw new TaskStatusException();
	    }

	    tarefa.setStatus(TaskStatus.CANCELADA);
	    repositorio.save(tarefa);

	    return tarefa;
	}
	
	
	
	
	
	
	
}


