package br.com.tarefas.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TarefaCategoriaRequest {

    private Integer id;

    @NotBlank(message = "Campo nome não pode estar vazio")
    @Size(max = 50, message = "Campo nome deve conter no maximo 50 caracteres.")
    private String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}