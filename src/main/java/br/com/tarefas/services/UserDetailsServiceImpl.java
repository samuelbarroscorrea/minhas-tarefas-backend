package br.com.tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.tarefas.model.User;
import br.com.tarefas.repository.UserRepository;
import br.com.tarefas.security.UserDetailsImpl;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository usuarioRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User usuario = usuarioRepository.findByNome(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		
		return UserDetailsImpl.build(usuario);
	}

}
 