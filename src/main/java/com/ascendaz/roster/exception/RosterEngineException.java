package com.ascendaz.roster.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Not Supported Rule")  
public class RosterEngineException extends Exception{

	private static final long serialVersionUID = 127145295086141737L;
	
	private String message;

	public RosterEngineException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
	
}
