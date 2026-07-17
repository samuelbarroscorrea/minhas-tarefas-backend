package br.com.tarefas.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.tarefas.controller.request.TaskCategoryFilter;
import br.com.tarefas.model.TaskCategory;
import jakarta.persistence.criteria.Predicate;

public class TaskCategorySpecification {

    public static Specification<TaskCategory> comFiltro(TaskCategoryFilter filtro) {

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