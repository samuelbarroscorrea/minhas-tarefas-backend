package br.com.tarefas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefas.controller.assembler.UserModelAssembler;
import br.com.tarefas.controller.request.UserRequest;
import br.com.tarefas.controller.response.UserResponse;
import br.com.tarefas.model.User;
import br.com.tarefas.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UserController {
	
	@Autowired
	private UserService usuarioService;
	
	@Autowired
	private UserModelAssembler assembler;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/{id}")
	public EntityModel<UserResponse> umUsuario(@PathVariable Integer id) {
		User usuario = usuarioService.getUsuarioPorId(id);
		EntityModel<UserResponse> usuarioResponse = assembler.toModel(usuario);
		return usuarioResponse;
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<UserResponse>> salvarUsuario(@Valid @RequestBody UserRequest usuarioReq) {
		User usuario = modelMapper.map(usuarioReq, User.class);
		User usuarioSalvo = usuarioService.salvar(usuario);
		EntityModel<UserResponse> usuarioModel = assembler.toModel(usuarioSalvo);
		
		return ResponseEntity.created(usuarioModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(usuarioModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<UserResponse>> atualizarUsuario(
			@PathVariable Integer id, @Valid @RequestBody UserRequest usuarioReq) {
		
		User usuario = modelMapper.map(usuarioReq, User.class);
		User usuarioSalvo = usuarioService.atualizar(id, usuario);
		EntityModel<UserResponse> usuarioModel = assembler.toModel(usuarioSalvo);
		
		return ResponseEntity.ok().body(usuarioModel);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirUsuario(@PathVariable Integer id) {
		usuarioService.deleteById(id);
	}

}
