package com.ascendaz.roster.exception;

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
