# Minhas Tarefas

API REST de gerenciamento de tarefas (backend)

## Tecnologias Utilizadas

* Java 17
* Spring Boot 4
* Spring Data JPA
* Spring Security
* JWT Authentication
* H2 Database
* Hibernate
* ModelMapper
* HATEOAS
* Maven

## Funcionalidades

### Autenticação

* Login com JWT
* Controle de acesso baseado em roles (USER e ADMIN)

### Tarefas

* Criar tarefa
* Listar tarefas
* Buscar tarefa por ID
* Pesquisar tarefas por descrição
* Iniciar tarefa
* Concluir tarefa
* Cancelar tarefa
* Excluir tarefa

### Usuários

* Criar usuário
* Buscar usuário por ID
* Atualizar usuário
* Excluir usuário

### Categorias

* Criar categoria
* Listar categorias
* Buscar categoria por ID
* Excluir categoria

## Segurança

O projeto utiliza Spring Security com JWT.

Após autenticação, o token deve ser enviado no header:

Authorization: Bearer SEU_TOKEN

## Banco de Dados

O projeto utiliza H2 Database em memória.

Console H2:

http://localhost:8080/minhastarefas/h2-console

## Executando o Projeto

Clone o repositório:

git clone https://github.com/seu-usuario/minhas-tarefas.git

Entre na pasta:

cd minhas-tarefas

Execute:

mvn spring-boot:run

A aplicação estará disponível em:

http://localhost:8080/minhastarefas

## Endpoints Principais

### Login

POST /api/auth/login

### Tarefas

GET /tarefa

GET /tarefa/{id}

POST /tarefa

PUT /tarefa/{id}/iniciar

PUT /tarefa/{id}/concluir

PUT /tarefa/{id}/cancelar

DELETE /tarefa/{id}

### Usuários

GET /usuario/{id}

POST /usuario

PUT /usuario/{id}

DELETE /usuario/{id}

### Categorias

GET /categoria

GET /categoria/{id}

POST /categoria

DELETE /categoria/{id}
