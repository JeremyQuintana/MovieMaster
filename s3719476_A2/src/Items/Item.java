package Items;

import Exceptions.*;

public abstract class Item {
	private String id;
	private String title;
	private String description;
	private String genre;
	private double fee;
	private HiringRecord currentlyBorrowed;
	
	HiringRecord[] hireHistory = new HiringRecord[10];
	
	public Item(String id, String title, String description, String genre) throws IdException {	//Constructor whenever a new item is created
		//throws an exception if the id length given is more than 3 characters long 
		if (id.length() > 5) {
			throw new IdException("must be 3 characters long");
		}
		
		//instantiate instance variables
		this.id = id;
		this.title = title;
		this.description = description;
		this.genre = genre;
	}
	
	/*
	 * borrowItem pseudocode
	 * BEGIN
	 * 	IF item is currently borrowed
	 * 		THROWS an exception
	 * 	ENDIF
	 * 
	 * 	WHILE not all the items in hireHistory have not been moved up
	 * 		CALCULATE the above hireHistory becomes the current hireHistory
	 *  ENDWHILE
	 *  
	 *  CALCULATE currently borrowed becomes the latest hireHistory
	 * END 
	 */
	public double borrowItem(String memberId, DateTime borrowDate, double fee) throws BorrowException {	//method to borrow movie
		//throws an exception if the item is currently borrowed
		if (currentlyBorrowed != null) {
			throw new BorrowException("is currently on loan");
		}
		
		//shifts the hiring record array elements up one index and removes the last element in the array
		for (int x = 8; x >= 0; x--) {
			hireHistory[x + 1] = hireHistory[x];
		}
		
		//creates a new hiring record and makes currently borrowed equal hire history to indicate the item is borrowed
		hireHistory[0] = new HiringRecord(id, memberId, borrowDate, fee);
		currentlyBorrowed = hireHistory[0];
			
		return fee;	//returns the rental fee
	}
	
	public double returnItem(DateTime returnDate, double lateFee) throws BorrowException {	//method to return a movie
		//throws an exception if the item is currently not borrowed
		if (currentlyBorrowed == null) {
			throw new BorrowException("is NOT currently on loan");
		}
		
		//calls the return item on the latest hireHistory array and makes currently borrowed equal null making it so it can be borrowed
		hireHistory[0].returnItem(returnDate, lateFee);
		currentlyBorrowed = null;
		
		return lateFee + fee;	//returns the total fee
	}
	
	public String getDetails() {	//gets details relevant to all subclasses
		//returns string details which are details for all subclasses
		String details =
				String.format("%-25s %s\n","ID:", id) +
				String.format("%-25s %s\n","Title:", title) + 
				String.format("%-25s %s\n","Genre:", genre) +
				String.format("%-25s %s\n","Decription:", description) +
				String.format("%-25s %s\n","Standard Fee:", fee);
		
		return details;		
	}
	
	public String getHireHistoryDetails() {	//returns a formatted string of details on all the elements in hireHistory
		int counter = 0;
		String details = "";
		
		//for every element in HireHistory the details gets added to the string details
		while ((counter < 10) && (hireHistory[counter] != null)) {
			details = details + hireHistory[counter].getDetails() + "\n";
			counter++;
		}
		
		return details;	//returns the concatenated details
	}
	
	public String toString() {	//returns string of details relevant to all subclasses in a format readable from a file
		String details = id + ":" + title + ":" + description + ":" + genre + ":";
		
		return details;
	}
	
	
	public int calcDateDiff(DateTime returnDate, DateTime borrowDate) {	//calculates the difference between the dates and returns the int difference
		DateTime date = new DateTime();
		return date.diffDays(returnDate,  borrowDate);
	}
	
	
	public HiringRecord getCurrentlyBorrowed() {
		return currentlyBorrowed;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getFee() {
		return fee;
	}
	
	public DateTime getBorrowDate() {
		return hireHistory[0].getBorrowDate();
	}
	
	public double getTotalFee() {	//returns the fee plus the late fee from the latest HireHistory element
		return fee + hireHistory[0].getLateFee();
	}
	
	public void setFee(double fee) {
		if (fee >= 0) {
			this.fee = fee;
		}
	}
}
