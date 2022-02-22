package com.learning.exception;

public class FoodNotFoundException extends Exception {
	
	public FoodNotFoundException(String e) {
		super(e);
	}
	
	@Override
	public String toString() {
		return super.getMessage();
	}
}
