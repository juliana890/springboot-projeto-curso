package com.aulaspring.SB_projetocurso.resources.exceptions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
