package Items;

import Exceptions.BorrowException;
import Exceptions.IdException;

public class Game extends Item{
	String[] platforms = new String[10];
	private boolean extended;
	final private double FEE = 20;
	
	public Game(String id, String title, String description, String genre, String[] platforms) throws IdException {	//runs when you create a new game
		super("G_" + id, title, description, genre);	//places variables into the parent class Item
		
		//instantiates remaining variables
		this.platforms = platforms;
		setFee(FEE);
	}
	
	public double borrowItem(String memberId, boolean extended, DateTime borrowDate) throws BorrowException {	//method when you borrow a movie item which calculates anything relevant only to borrowing a game
		this.extended = extended;	//instantiate variable
		
		//depending on whether extended is true or false the super method borrowItem runs with different fees then returns the value of the super borrowItem method
		double tempValue;
		if (extended == true) {
			tempValue = super.borrowItem(memberId, borrowDate, FEE + 5);
		}
		else {
			tempValue = super.borrowItem(memberId, borrowDate, FEE);
		}
		hireHistory[0].setExtended(extended);
		return tempValue;
	}
	
	public double returnItem(DateTime returnDate) throws BorrowException {	//method when you return a movie which calculates anything relevant only to movies when returning a movie
		int daysOnLoan = super.calcDateDiff(returnDate, hireHistory[0].getBorrowDate());	//calculates the difference between the difference of the return and borrow date to find out how long it was on loan
		
		//depending on how long the game was on loan the super method returnItem is run with different fee amounts which then return the total fee
		if (daysOnLoan > 22) {
			if (extended == true) {
				return super.returnItem(returnDate, (((FEE + 5) / 4) * (daysOnLoan - 22)));
			}
			else {
				return super.returnItem(returnDate, (((FEE + 5) / 2) * (daysOnLoan - 22)));
			}
		}
		else {
			return super.returnItem(returnDate, 0);
		}
	}
	
	/*
	 * getDetails pseudocode
	 * BEGIN
	 * 	GET details from superclass getDetails
	 * 
	 * 	IF currently borrowed is empty
	 * 		IF extended is true
	 * 			CALCULATE details adds on with details that it's extended
	 * 		ELSE
	 * 			CALCULATE details adds on with details that it's currently borrowed
	 * 		ENDIF
	 * 	ELSE
	 * 		CALCULATE details adds on with details that it's not borrowed
	 * 	ENDIF
	 * 
	 * 	CALCULATE details with details of the method from getPlatforms
	 * 	CALCULATE details with details to add that the rental period is 22 days
	 * 	
	 * 	CALCULATE details with details that adds with the HireHistory details
	 * END
	 * 			
	 */
	public String getDetails() {	//returns a string of formatted details
		//gets the details from the parent getDetails method
		String details = super.getDetails();
		
		//adds onto the received details which details specifically for the movie class
		if (getCurrentlyBorrowed() != null) {
			if (extended == true) {
				details += String.format("%-25s %s\n","On Loan:", "Extended");
			}
			else {
				details += String.format("%-25s %s\n","On Loan:", "Yes");
			}
		}
		else {
			details += String.format("%-25s %s\n","On Loan:", "No");
		}
		
		details += String.format("%-25s %s\n","Platforms:", getPlatforms());
		details += String.format("%-25s %s\n","Rental Period:", "22 Days");
		details += "\n\nBorrowing Record\n";
		
		details += super.getHireHistoryDetails();	//gets all the hiringRecords for the movies and adds it to the existing details
		
		return details;
	}
	
	public String toString() {	//returns a string of all the details separated by ":" to be placed and read to and from a file
		String details = super.toString() + getPlatforms() + ":";	//gets the details from the parent class
		
		//adds to the details obtained from the parent class with details that are only relevant to the movie class
		if (getCurrentlyBorrowed() != null) {
			if( extended == true) {
				details += "E";
			}
			else {
				details += "Y";
			}
		}
		else {
			details += "N";
		}
		
		//adds gets every toString in the array of hireHistory to add onto the details
		for(int x = 0; x < hireHistory.length && hireHistory[x] != null; x++) {
			details += ":" + hireHistory[x].toString();
		}
		
		return details;
	}
	
	
	public String getPlatforms() {	//method to return platforms in a formatted string rather than an array
		String details;
		int counter = 0;
		
		//reads each element in the array platform and adds it to the details then after returns the details after reading all the elements
		details = platforms[0];
		counter++;
		while (counter < platforms.length && platforms[counter] != null) {
			details = details + ", " + platforms[counter];
			counter++;
		}
		return details;
	}
	
	public boolean getExtended() {
		return extended;
	}
}
