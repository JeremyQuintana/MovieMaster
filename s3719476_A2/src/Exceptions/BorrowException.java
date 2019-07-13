package Exceptions;

public class BorrowException extends Exception {
	public BorrowException() {
		super("Invalid ID");
	}
	
	public BorrowException (String message) {
		super("Error - The item " + message);
	}
}
