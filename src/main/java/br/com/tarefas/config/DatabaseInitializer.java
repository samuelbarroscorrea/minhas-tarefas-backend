package br.com.tarefas.config;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.tarefas.model.ERole;
import br.com.tarefas.model.Role;
import br.com.tarefas.model.Tarefa;
import br.com.tarefas.model.TaskCategory;
import br.com.tarefas.model.TaskStatus;
import br.com.tarefas.model.User;
import br.com.tarefas.repository.RoleRepository;
import br.com.tarefas.repository.TaskCategoryRepository;
import br.com.tarefas.repository.TaskRepository;
import br.com.tarefas.repository.UserRepository;

@Configuration
@Profile("dev")
public class DatabaseInitializer {
	
	@Autowired
	private UserRepository usuarioRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private TaskCategoryRepository CategoriaRepository;
	
	@Autowired
	private TaskRepository tarefaRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Bean
	CommandLineRunner executar() {
		return args -> {
			
			Role roleAdmin = new Role(ERole.ROLE_ADMIN);
			roleAdmin = roleRepository.save(roleAdmin);
			
			User usuario = new User();
			usuario.setNome("Admin");
			usuario.setSenha(encoder.encode("123456"));
			usuario.setRoles(Set.of(roleAdmin));
			usuarioRepository.save(usuario);
			
			TaskCategory categoria = new TaskCategory();
			categoria.setNome("Estudos");
			CategoriaRepository.save(categoria);
			
			Tarefa tarefa = new Tarefa();
			tarefa.setDescricao("Aprender Spring Boot");
			tarefa.setDataEntrega(LocalDate.now().plusDays(1));
			tarefa.setStatus(TaskStatus.ABERTO);
			tarefa.setVisivel(true);
			tarefa.setCategoria(categoria);
			tarefa.setUsuario(usuario);
			tarefaRepository.save(tarefa);
			
			Tarefa tarefa2 = new Tarefa();
			tarefa2.setDescricao("Aprender Spring Data JPA");
			tarefa2.setDataEntrega(LocalDate.now().plusDays(1));
			tarefa2.setStatus(TaskStatus.ABERTO);
			tarefa2.setVisivel(true);
			tarefa2.setCategoria(categoria);
			tarefa2.setUsuario(usuario);
			
			tarefaRepository.save(tarefa2);
			
		};
	}
}
