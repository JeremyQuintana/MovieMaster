package Items;

import Exceptions.BorrowException;
import Exceptions.IdException;

public class Movie extends Item{
	private boolean isNewRelease;
	final private double NEW_RELEASE_SURCHARGE = 5.00;
	final private double WEEKLY_RELEASE_SURCHARGE = 3.00;
	
	public Movie(String id, String title, String description, String genre, boolean isNewRelease) throws IdException {	//runs when you create a new movie
		super("M_" + id, title, description, genre);	//places variables into the parent class Item
		
		//instantiates remaining variables
		this.isNewRelease = isNewRelease;
		if(isNewRelease == true) setFee(NEW_RELEASE_SURCHARGE);
		else setFee(WEEKLY_RELEASE_SURCHARGE);
	}
	
	public double borrowItem(String memberId, DateTime borrowDate) throws BorrowException {	//method when you borrow a movie item which calculates anything relevant only to borrowing a movie
		//depending on whether isNewRelease is true the borrowItem method in the parent class receives different fee amounts
		if (isNewRelease == true) {
			return super.borrowItem(memberId, borrowDate, NEW_RELEASE_SURCHARGE);
		}
		else {
			return super.borrowItem(memberId, borrowDate, WEEKLY_RELEASE_SURCHARGE);
		}
	}
	
	/*
	 * returnItem pseudocode
	 * BEGIN
	 * 	IF isNewRelease is true
	 * 		IF daysOnLoan is more than 2
	 * 			CALCULATE the lateFee with the overdue rate and how long it was overdue
	 * 			RUN the super method of returnItem with relevant details calculated fee
	 * 		ELSE
	 * 			RUN the super method of returnItem with relevant details but fee is 0
	 * 		ENDIF
	 * 	ELSE
	 * 		IF daysOnLoan is more than 7
	 * 			CALCULATE the lateFee with the overdue rate and how long it was overdue
	 * 			RUN the super method of returnItem with relevant details calculated fee
	 * 		ELSE
	 * 			RUN the super method of returnItem with relevant details but fee is 0
	 * 		ENDIF
	 * 	ENDIF
	 */
	
	public double returnItem(DateTime returnDate) throws BorrowException {	//method when you return a movie which calculates anything relevant only to movies when returning a movie
		//calculates the late fee and calls the parent returnItem method passing through the relevant lateFee according to the overdue rate and amount of days on loan
		int daysOnLoan = super.calcDateDiff(returnDate, hireHistory[0].getBorrowDate());
		if (isNewRelease == true) {
			if (daysOnLoan > 2) {
				return super.returnItem(returnDate, ((daysOnLoan - 2) * (NEW_RELEASE_SURCHARGE / 2)));
			}
			else {
				return super.returnItem(returnDate, 0);
			}
		}
		else {
			if (daysOnLoan > 7) {
				return super.returnItem(returnDate, ((daysOnLoan - 7) * (WEEKLY_RELEASE_SURCHARGE / 2)));
			}
			else {
				return super.returnItem(returnDate, 0);
			}
		}
	}
	
	public String getDetails() {	//returns a string of formatted details
		//gets the details from the parent getDetails method
		String details = super.getDetails();
		
		//adds onto the received details which details specifically for the movie class
		if (getCurrentlyBorrowed() != null) {
			details += String.format("%-25s %s\n","On Loan:", "Yes");
		}
		else {
			details += String.format("%-25s %s\n","On Loan:", "No");
		}
		if (isNewRelease == true) {
			details += String.format("%-25s %s\n","Rental Period:", "2 Days");
		}
		else {
			details += String.format("%-25s %s\n","Rental Period:", "7 Days");
		}
		
		details += "\n\nBorrowing Record\n" + super.getHireHistoryDetails();	//gets all the hiringRecords for the movies and adds it to the existing details
		
		return details;
	}
	
	public String toString() {	//returns a string of all the details separated by ":" to be placed and read to and from a file
		String details = super.toString();	//gets the details from the parent class
		
		//adds to the details obtained from the parent class with details that are only relevant to the movie class
		if (isNewRelease == true) {
			details = details + "NR:";
		}
		else {
			details = details + "WK:";
		}
		
		if (getCurrentlyBorrowed() != null) {
			details = details + "Y";
		}
		else {
			details = details + "N";
		}
		
		//adds gets every toString in the array of hireHistory to add onto the details
		for(int x = 0; x < hireHistory.length && hireHistory[x] != null; x++) {
			details += ":" + hireHistory[x].toString();
		}
		
		return details;
	}
	
	
	public boolean getIsNewRelease() {
		return isNewRelease;
	}
}
