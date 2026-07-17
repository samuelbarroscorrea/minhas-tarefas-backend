package br.com.tarefas.controller.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.tarefas.controller.UserController;
import br.com.tarefas.controller.response.UserResponse;
import br.com.tarefas.model.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<UserResponse>> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public EntityModel<UserResponse> toModel(User usuario) {

        UserResponse usuarioResp = mapper.map(usuario, UserResponse.class);

        EntityModel<UserResponse> usuarioModel = EntityModel.of(usuarioResp,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class)
                                .umUsuario(usuarioResp.getId())
                ).withSelfRel()
        );

        return usuarioModel;
    }
}