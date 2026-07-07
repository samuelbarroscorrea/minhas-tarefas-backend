package br.com.tarefas.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaCategoria;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

	public List<Tarefa> findByDescricao(String descricao);
	
	public Page<Tarefa> findByDescricaoLike(String descricao, Pageable pageable);

	public List<Tarefa> findByCategoria(TarefaCategoria categoria);
	
	public List<Tarefa> findByIdGreaterThanOrderByIdAsc(Integer cursor, Pageable pageable);
	
	@Query("select t from Tarefa t inner join t.categoria c where c.nome = ?1")
	public List<Tarefa> findByNomeCategoria(String nomeCategoria);
	
	public List<Tarefa> tarefasPorCategoria(String nomeCategoria);
}
