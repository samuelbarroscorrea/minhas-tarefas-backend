package br.com.tarefas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.tarefas.controller.response.ErrorResponse;
import br.com.tarefas.exception.TaskStatusException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse entityNotFoundHandler(EntityNotFoundException ex) {
		return new ErrorResponse("Recurso não encontrado");
	}
	
	@ExceptionHandler(TaskStatusException.class)
	public ResponseEntity<?> alteraStatusTarefaHandler(TaskStatusException ex) {

	    Problem problem = Problem.create()
	            .withTitle("Método não permitido")
	            .withDetail("Você não pode realizar esta operação: " + ex.getMessage());

	    return ResponseEntity
	            .status(HttpStatus.METHOD_NOT_ALLOWED)
	            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
	            .body(problem);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
	        HttpMessageNotReadableException ex,
	        HttpHeaders headers,
	        HttpStatusCode status,
	        WebRequest request) {

	    ErrorResponse erro = new ErrorResponse(
	            null,
	            "JSON inválido ou dados em formato incorreto"
	    );

	    return ResponseEntity
	            .badRequest()
	            .body(erro);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse propertyReferenceHandler(
	        PropertyReferenceException ex) {

	    return new ErrorResponse(
	            "Campo de ordenação inválido: " + ex.getPropertyName()
	    );
	}
	
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ErrorResponse(x.getField(), x.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorResponse entityBadCredentialsException(BadCredentialsException ex) {
    	return new ErrorResponse("Nome de Usuário e/ou senha inválidos");
    }
}