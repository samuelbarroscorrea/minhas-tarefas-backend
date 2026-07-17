package br.com.tarefas.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.tarefas.controller.request.TarefaFiltro;
import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;
import br.com.tarefas.repository.TarefaRepository;
import br.com.tarefas.repository.specification.TarefaSpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository repositorio;
	
	public Page<Tarefa> buscar(TarefaFiltro filtro, Pageable pageable) {

	    Specification<Tarefa> specification =
	            TarefaSpecification.comFiltro(filtro);

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
		
		if (!TarefaStatus.ABERTO.equals(tarefa.getStatus())) {
			throw new TarefaStatusException();
		}
		 
		tarefa.setStatus(TarefaStatus.EM_ANDAMENTO);
		repositorio.save(tarefa);
		return tarefa;
		
	}
	
	public Tarefa concluirTarefaPorId(Integer id) {
		Tarefa tarefa = getTarefaPorId(id);
		
		if (TarefaStatus.CANCELADA.equals(tarefa.getStatus())) {
			throw new TarefaStatusException();
		}
		 
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		repositorio.save(tarefa);
		return tarefa;
		
	}
	
	public Tarefa cancelarTarefaPorId(Integer id) {
	    Tarefa tarefa = getTarefaPorId(id);

	    if (TarefaStatus.CONCLUIDA.equals(tarefa.getStatus())) {
	        throw new TarefaStatusException();
	    }

	    tarefa.setStatus(TarefaStatus.CANCELADA);
	    repositorio.save(tarefa);

	    return tarefa;
	}
	
	
	
	
	
	
	
}


