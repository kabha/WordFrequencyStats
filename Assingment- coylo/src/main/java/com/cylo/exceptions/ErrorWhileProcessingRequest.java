package com.cylo.exceptions;

public class ErrorWhileProcessingRequest extends Exception {

	private static final long serialVersionUID = 1L;
	private boolean badRequest = false;

	public ErrorWhileProcessingRequest() {
		super();
	}

	public ErrorWhileProcessingRequest(String message) {
		super(message);
	}

	public ErrorWhileProcessingRequest(String message, boolean badRequest) {
		super(message);
		this.badRequest = badRequest;
	}

	public boolean isBadRequest() {
		return badRequest;
	}

}
