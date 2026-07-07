package br.com.tarefas.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.tarefas.exception.TarefaStatusException;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaStatus;
import br.com.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository repositorio;
	
	public Page<Tarefa> getTodasTarefas(Pageable pageable) {
		return repositorio.findAll(pageable);
	}
	
	public List<Tarefa> getTarefasPorCursor(Integer cursor, Pageable pageable) {
		return repositorio.findByIdGreaterThanOrderByIdAsc(cursor, pageable);
	}
	
	public Page<Tarefa> getTarefasPorDescricao(String descricao, Pageable pageable) {
		return repositorio.findByDescricaoLike("%" + descricao + "%", pageable);
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


