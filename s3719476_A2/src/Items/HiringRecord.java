package Items;

public class HiringRecord {
	private String hireId;
	private double rentalFee;
	private double lateFee;
	private DateTime borrowDate;
	private DateTime returnDate;
	private boolean extended;
	
	public HiringRecord(String id, String memberId, DateTime borrowDate, double rentalFee) {	//whenever an item is borrowed a new HiringRecord is created and the constructor runs
		//instantiates all the variables that are obtained when borrowing
		this.hireId = id + "_" + memberId + "_" + borrowDate.getEightDigitDate();
		this.borrowDate = borrowDate;
		this.rentalFee = rentalFee;
	}
	
	public double returnItem(DateTime returnDate, double lateFee) {	//when a movie is returned the remaining instance variables are instantiated
		//instantiate the remaining instance variables
		this.returnDate = returnDate;
		this.lateFee = lateFee;
		
		//returns totalFee
		return lateFee + rentalFee;
	}
	
	/*
	 * getDetails pseudocode
	 * 
	 * BEGIN
	 * 	CALCULATE the concatenation of the hireId and formatted borrowDate
	 * 	
	 * 	IF returnDate is not equal to null
	 * 		CALCULATE the concatenation of the remaining instance variables with the previous details
	 * 	ENDIF
	 * END
	 */
	public String getDetails() {	//returns a formatted string on the instance variables of the class
		//creates details variable to hold the instance variables that are available when an item is borrowed
		String details =
				String.format("%-25s %s\n","Hire ID:", hireId) +
				String.format("%-25s %s\n","Borrow Date:", borrowDate.getFormattedDate());
		
		//if the item has been returned then the details also adds the remaining instance variables
		if (returnDate != null) {
			details +=
					String.format("%-25s %s\n","Return Date:", returnDate.getFormattedDate()) +
					String.format("%-25s %s\n","Fee:", rentalFee) +
					String.format("%-25s %s\n","Late Fee:", lateFee) + 
					String.format("%-25s %s\n","Total Fees:", rentalFee + lateFee);
		}
		
		return details;
	}
	
	public String toString() {	//returns a string of all the details separated by ":" to be placed and read to and from a file
		//creates a details variable to hold the instance variables for both game and movie classes
		String details = hireId + ":" + rentalFee + ":" + lateFee + ":";
		
		//if the item is a game then an extra variable needs to be added, extended or else there is no extended variable recorded
		if (hireId.substring(0, 1).equals("G")) details += extended + ":" + borrowDate + ":" + returnDate;
		else details += borrowDate + ":" + returnDate;
		
		return details;
	}
	
	
	public DateTime getBorrowDate() {
		return borrowDate;
	}
	
	public double getLateFee() {
		return lateFee;
	}
	
	public void setExtended(boolean extended) {
		if(hireId.substring(0, 1).equals("G")) this.extended = extended;
	}
}
