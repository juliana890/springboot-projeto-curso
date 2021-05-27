package com.aulaspring.SB_projetocurso.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aulaspring.SB_projetocurso.services.exceptions.AuthorizationException;
import com.aulaspring.SB_projetocurso.services.exceptions.DataIntegrityException;
import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

//Criamos essa classe para tratar possíveis erros no controlador Rest
@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class) //Tratador de exceções do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND; //Error 404
		
		StandardError err = new StandardError(status.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(status.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ValidationError err = new ValidationError(status.value(), "Erro de Validação", System.currentTimeMillis());
		
		//Para acessar os erros de campos
		for(FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(AuthorizationException.class) //Tratador de exceções do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.FORBIDDEN; //Error 404
		
		StandardError err = new StandardError(status.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(status).body(err);
	}

}
