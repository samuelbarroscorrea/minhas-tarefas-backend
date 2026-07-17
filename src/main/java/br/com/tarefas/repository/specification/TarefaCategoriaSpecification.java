package br.com.tarefas.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.tarefas.controller.request.TarefaCategoriaFiltro;
import br.com.tarefas.model.TarefaCategoria;
import jakarta.persistence.criteria.Predicate;

public class TarefaCategoriaSpecification {

    public static Specification<TarefaCategoria> comFiltro(TarefaCategoriaFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNome() != null && !filtro.getNome().isBlank()) {

                predicates.add(
                    cb.like(
                        cb.lower(root.get("nome")),
                        "%" + filtro.getNome().toLowerCase() + "%"
                    )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}