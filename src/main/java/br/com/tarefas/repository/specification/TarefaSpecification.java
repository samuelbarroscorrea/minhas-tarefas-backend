package br.com.tarefas.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.tarefas.controller.request.TarefaFiltro;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TarefaCategoria;
import br.com.tarefas.model.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class TarefaSpecification {

	public static Specification<Tarefa> comFiltro(TarefaFiltro filtro) {

	    return (root, query, cb) -> {

	        List<Predicate> predicates = new ArrayList<>();

	        Join<Tarefa, TarefaCategoria> categoriaJoin = null;
	        Join<Tarefa, Usuario> usuarioJoin = null;


	        if (filtro.getDescricao() != null 
	                && !filtro.getDescricao().isBlank()) {

	            predicates.add(
	                cb.like(
	                    cb.lower(root.get("descricao")),
	                    "%" + filtro.getDescricao().toLowerCase() + "%"
	                )
	            );
	        }


	        if (filtro.getStatus() != null) {

	            predicates.add(
	                cb.equal(
	                    root.get("status"),
	                    filtro.getStatus()
	                )
	            );
	        }


	        if (filtro.getCategoria() != null 
	                && !filtro.getCategoria().isBlank()) {

	            categoriaJoin = root.join("categoria");

	            predicates.add(
	                cb.equal(
	                    categoriaJoin.get("nome"),
	                    filtro.getCategoria()
	                )
	            );
	        }


	        if (filtro.getUsuario() != null) {

	            usuarioJoin = root.join("usuario");

	            predicates.add(
	                cb.equal(
	                    usuarioJoin.get("id"),
	                    filtro.getUsuario()
	                )
	            );
	        }


	        if (filtro.getDataInicio() != null) {

	            predicates.add(
	                cb.greaterThanOrEqualTo(
	                    root.get("dataEntrega"),
	                    filtro.getDataInicio()
	                )
	            );
	        }


	        if (filtro.getDataFim() != null) {

	            predicates.add(
	                cb.lessThanOrEqualTo(
	                    root.get("dataEntrega"),
	                    filtro.getDataFim()
	                )
	            );
	        }


	        return cb.and(
	                predicates.toArray(new Predicate[0])
	        );
	    };
	}
}