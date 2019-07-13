package Exceptions;

public class IdException extends Exception{
	
	public IdException() {
		super("Invalid ID");
	}
	
	public IdException(String message) {
		super("Error - The ID " + message);
	}
}
