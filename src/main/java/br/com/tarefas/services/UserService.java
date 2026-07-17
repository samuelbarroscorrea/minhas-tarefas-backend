package br.com.tarefas.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.tarefas.security.UserDetailsImpl;
import br.com.tarefas.controller.response.JwtResponse;
import br.com.tarefas.model.Role;
import br.com.tarefas.model.User;
import br.com.tarefas.repository.RoleRepository;
import br.com.tarefas.repository.UserRepository;
import br.com.tarefas.security.JwtUtils;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository usuarioRepositorio;
	
	@Autowired
	private RoleRepository roleRepositorio;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	public User getUsuarioPorId(Integer id) {
		return usuarioRepositorio.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public User salvar(User usuario) {
		Set<Role> roles = getRoles(usuario);
		usuario.setRoles(roles);
		usuario.setSenha(encoder.encode(usuario.getSenha()));
		return usuarioRepositorio.save(usuario);
	}
	
	public User atualizar(Integer id, User usuario) {
		if(!usuarioRepositorio.existsById(id)) {
			throw new EntityNotFoundException();
		}
		usuario.setId(id);
		return salvar(usuario);
	}
	
	public void deleteById(Integer id) {
		usuarioRepositorio.deleteById(id);
	}
	
	private Set<Role> getRoles(User usuario) {
		Set<Role> rolesBanco = usuario.getRoles()
				.stream()
				.map(role -> roleRepositorio.findByName(role.getName()))
				.collect(Collectors.toSet());
		
		return rolesBanco;
	}

	public JwtResponse autenticaUsuario(String nome, String senha) {
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nome, senha));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return new JwtResponse(jwt, userDetails.getId(),
				userDetails.getUsername(), roles);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
