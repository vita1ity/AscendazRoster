package com.ascendaz.roster.exception;

public class RosterExceptionListener {
	private volatile boolean isException;
	private RosterEngineException ex;
	
	public void rosterExceptionOccured(RosterEngineException ex) {
		isException = true;
		this.ex = ex;
	}

	public boolean isException() {
		return isException;
	}

	public RosterEngineException getEx() {
		return ex;
	}
	
	
	
}
