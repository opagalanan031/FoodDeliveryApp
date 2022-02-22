package com.learning.exception;

public class DataNotFoundException extends RuntimeException {
	
	public DataNotFoundException(String e) {
		super(e);
	}
	
	@Override
	public String toString() {
		return super.getMessage();
	}
}
