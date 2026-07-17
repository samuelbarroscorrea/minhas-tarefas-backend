package br.com.tarefas.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.tarefas.model.TaskCategory;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer>, JpaSpecificationExecutor<TaskCategory> {
	
}
