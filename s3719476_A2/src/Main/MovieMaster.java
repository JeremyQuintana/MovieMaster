package Main;

import java.util.Scanner;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

import Exceptions.*;
import Items.*;

public class MovieMaster {
	Scanner input = new Scanner(System.in);	//declare variable for input from scanner by user
	Item[] item = new Item[99];	//instantiate item array with size of 99
	int itemCounter = 0;	//declare itemCounter to keep track of next free location on the array item
	String fileName = "record.txt";	//declare the file name that the system reads from
	
	public MovieMaster() {	//starting method when the MovieMaster class is created
		fileRead();	//reads from the file to place inputs into the system
	}
	
	public void menu() {
		String screen;	//declare screen variable to print details to the console
		
		boolean inputVerified = false;	//declare boolean inputVerified which should always be false as the menu keeps looping
		while (inputVerified == false) {	//always loops after each function
			System.out.println("__________________________________________________________");	//a series of strings are created to add to screen to create a final display to the console
			screen = "*** Movie Master System Menu ***\n";
			screen += String.format("%-25s %s\n","Add Item", "A");
			screen += String.format("%-25s %s\n","Borrow Item", "B");
			screen += String.format("%-25s %s\n","Return Item", "C");
			screen += String.format("%-25s %s\n","Display Details", "D");
			screen += String.format("%-25s %s\n","Seed Data", "E");
			screen += String.format("%-25s %s\n","Exit Program", "X");
			screen += String.format("%-25s %s","Enter Selection:", "");
			System.out.print(screen);
			//switch statement which obtains the users input puts it all into lower case  and compares it to all values in the switch statement
			switch (input.nextLine().toLowerCase()) {
			case "a":
				addItem();
				break;
			case "b":
				borrowItem();
				break;
			case "c":
				returnItem();
				break;
			case "d":
				displayDetails();
				break;
			case "e":
				seedData();
				break;
			case "x":
				exitProgram();
				break;
			default:	//if the input matched none of the cases then
				System.out.println("\n__________________________________________________________");	//prints error to the console to select one of the values and repeats the loop
				System.out.println("\n\nInvalid input, please enter one of the options below\n");
			}
		}
	}
	
	public void addItem() {	//method that adds a new item to the item array
		//Declare boolean validated value and tempValue for validation of appropriate inputs
		boolean validated;	
		String tempValue;	
		
		System.out.print("\n\n__________________________________________________________\n");	//validates the user to choose whether the item the user wants to add is a game or movie
		String type = validateChoice("G", "M", String.format("%-25s %s\n","Add Game", "G") +  String.format("%-25s %s\n","Add Movie", "M") + String.format("%-25s %s","Enter Selection:", ""));
		
		do {	//loops if validation is false until an validated input passes is placed
			validated = true;
			//prompts the user for an input then stores this input into tempValue
			System.out.print(String.format("%-25s %s","Enter ID:", ""));
			tempValue = input.nextLine().toUpperCase();
			
			//validates the tempValue with appropriate validations
			if (validateIdLength(tempValue) == false) validated = false;
			if (validateIdExistance(tempValue) == true) {
				validated = false;
				System.out.println("Error - ID already exists in the system"); //prints error that it already exists
			}
		}while(validated == false);	//loops if a validation error occurred
		String id = tempValue;	//if the input value passes the validations then variable id now holds the tempValue
		
		//prompts the user for an input then stores this input into tempValue
		System.out.print(String.format("%-25s %s","Enter Title:", ""));
		String title = input.nextLine();
		
		//prompts the user for an input then stores this input into tempValue
		System.out.print(String.format("%-25s %s","Enter Genre:", ""));
		String genre = input.nextLine();
		
		//prompts the user for an input then stores this input into tempValue
		System.out.print(String.format("%-25s %s","Enter Description:", ""));
		String description = input.nextLine();
		
		if (type.equals("M")) {	//if statement to find what type of movie the user is making to give appropriate prompts for each type. If user picked a movie
			//Declares isNewRelease then gives the user a choice of yes or no then assigns isNewRelease to the relevant value
			boolean isNewRelease;
			if (validateChoice("Y", "N", String.format("%-25s %s","Enter new release (Y/N):", "")).equals("Y")) isNewRelease = true;
			else isNewRelease = false;
			
			try {	//try statement to catch if an id error passed by the validation
				//throws an id exception if the id already exists in the system
				if (validateIdExistance(id) == true) throw new IdException("is invalid as another item has the same ID");
				
				//creates a new movie and moves itemCounter up for the next movie
				item[itemCounter] = new Movie(id, title, description, genre, isNewRelease);
				itemCounter++;
				System.out.print("\nMovie added successfully for movie ID: M_" + id + "\n\n");
			}
			catch(IdException e) {	//if adding a movie does catch an error
				System.out.println("\n" + e.getMessage() + "\n\n");	//the console prints an error message
			}
			
		}
		else if(type.equals("G")) {	//if the user chooses they want to add a game
			//declare variables relevant to store a series of inputs from the user
			boolean finished = false;
			int counter = 0;
			String[] consoles = new String[10];
			
			while (finished == false) {	//loops until the user has finished entering consoles
				//prompts the user to enter a console then assigns this console to an empty array element in consoles
				System.out.print(String.format("%-25s %s","Enter One Console:", ""));
				tempValue = input.nextLine();
				consoles[counter] = tempValue;
				
				//validation which gives user a choice if they want to add another console if not finished equals true and it exits the array
				if (validateChoice("Y", "N", String.format("%-25s %s","Enter another platform (Y/N):", "")).equals("N")) finished = true;
			}
			
			try {	//try block to see if any id error passed by the validation
				if (validateIdExistance(id) == true) throw new IdException("is invalid as another item has the same ID");	//if the id already exists in the system it throws an exception
				
				//creates a new movie and moves itemCounter up for the next movie
				item[itemCounter] = new Game(id, title, description, genre, consoles);
				itemCounter++;
				System.out.print("\nGame added successfully for game ID: G_" + id + "\n\n");
			}
			catch(IdException e) {	//if it does catch an id exception
				System.out.println("\n" + e.getMessage() + "\n\n");	//it prints an error to the console
			}
		}
	}
	
	public void borrowItem() {	//method if the user wants to borrow an item
		//Declare boolean validated value and tempValue for validation of appropriate inputs
		boolean validated;
		String tempValue;
		
		System.out.print("\n\n__________________________________________________________\n");
		
		//prompts the user and validates this id input to be acceptable to borrow a movie
		int itemLocation = 0;
		do {	//loops if validation is false
			validated = true;
			//prompts the user for an input then stores this input into tempValue
			System.out.print(String.format("%-25s %s","Enter Item ID:", ""));
			tempValue = input.nextLine().toUpperCase();
			
			//id validation relevant to borrow if false the loop repeats
			if (validateIdExistance(tempValue) == false) {
				validated = false;
				System.out.println("Error - ID cannot be found in the system\n");	//tells the user the id was not found
			}
			else {
				if (validateItemBorrowed(itemLocation) == true) {
					validated = false;
					System.out.println("Error - Item is currently on loan\n");	//prints an error telling the user its currently on loan
				}
			}
			itemLocation = calcItemLocation(tempValue);	//finds the location of the array index of the value input and assigns it to itemLocation
		}while (validated == false);	//if validated ever becomes false it loops and prompts the user for an id again
		String id = tempValue;	//after an input is validated a variable id holds the input
		
		do {	//loop to get input and validate memberId
			validated = true;
			
			//prompts the user for an input then stores this input into tempValue
			System.out.print(String.format("%-25s %s","Enter Member ID:", ""));
			tempValue = input.nextLine().toUpperCase();
			
			if (validateIdLength(tempValue) == false) validated = false;	//validates that the input id is of length 3 else validate becomes false
		}while (validated == false);	//if validated is false then it loops prompting user for id again
		String memberId = tempValue;
		
		//prompts the user to enter the how many days in advanced the user wants to borrow and holds it in advanced borrow
		System.out.print(String.format("%-25s %s","Enter Advanced Borrowing:", ""));
		int advancedBorrow = input.nextInt();
		input.nextLine();
		
		DateTime borrowDate = getDate(advancedBorrow);	//borrowDate is instantiated and calculated from by getting the current date plus the amount of advancedBorrow
		
		String screen = null;
		if (item[itemLocation].getId().substring(0, 1).equals("G")) {	//if the item the user wants to borrow is a game
			//gives user choice to choose if they want extended hire and places their choice to extended
			boolean extended;
			if (validateChoice("Y", "N",String.format("%-25s %s","Apply for extended hire (Y/N):", "")).equals("Y")) extended = true;
			else extended = false;
			
			try {	//if any exception passed by the validation
				if (validateIdExistance(id) == false) throw new IdException("could not be found in the system");	//if the id input was not found in the system throw an id exception
				
				//borrow the game at item location with the variables obtained and displays relevant details to the user
				((Game)item[itemLocation]).borrowItem(memberId, extended, borrowDate);
				screen = "The item " + item[itemLocation].getTitle() + " costs $" + item[itemLocation].getFee() + " and is due on: " + getDate(advancedBorrow + 22).getFormattedDate();
				System.out.println("\n" + screen + "\n");
			}
			catch(BorrowException e) {	//if a borrow exception is found
				System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error message
			}
			catch(IdException e) {	//if an id exception is found
				System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error message
			}
		}
		else if (item[itemLocation].getId().substring(0, 1).equals("M")) {	//if the item the user wants to borrow is a movie
			try {	//if an error slipped by validation
				if (validateIdExistance(id) == false) throw new IdException("could not be found in the system");	//if the id input does not exist in the system it throws an id exception
				
				//borrows the movie at the item location with the variables obtained and prints relevant borrow details
				((Movie)item[itemLocation]).borrowItem(memberId, borrowDate);
				screen = "The item " + item[itemLocation].getTitle() + " costs $" + item[itemLocation].getFee() + " and is due on: ";
				if (((Movie)item[itemLocation]).getIsNewRelease() == false) screen += getDate(advancedBorrow + 7).getFormattedDate();
				else screen += getDate(advancedBorrow + 2).getFormattedDate();
				System.out.println("\n" + screen + "\n");
			}
			catch(BorrowException e) {	//if a borrow exception is found
				System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error message
			}
			catch(IdException e) {	//if a id exception is found
				System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error message
			}
		}
	}
	
	/* returnItem pseudocode
	 * BEGIN
	 * 		WHILE tempValue is not validated
	 * 			PRINT to console asking for id
	 * 			CALCULATE tempValue equals the input
	 * 
	 *		 	CALCULATE length of tempValue
	 * 			IF tempValue is not 3 long
	 * 				CALCULATE validation equals false
	 * 			ENDIF
	 * 			CALCULATE if tempValue already exists in the system
	 * 			IF tempValue exists in the system
	 * 				CALCULATE the position of tempValue index in the system
	 * 				CALCULATE if tempValue is not borrowed
	 * 				IF tempValue is not borrowed
	 * 					CALCULATE validation is false
	 * 				ENDIF
	 * 			ENDIF
	 * 		ENDWHILE
	 * 		
	 * 		PRINT to console asking for amount of daysOnLoan
	 * 		CALCULATE tempValue equals the input
	 * 		
	 * 		CALCULATE tempValue index position in system
	 * 		CALCULATE the returnDate
	 * 		IF tempValue is an instance of game
	 * 			RUN return method for game with variables collected
	 * 		ELSEIF tempValue is an instance of movie
	 * 			RUN return method for movie with variables collected
	 * 		ENDIF
	 * 		
	 * 		PRINT relevant return details
	 * END
	 */
	
	public void returnItem() {	//method if the user wants to return an item
		//Declare boolean validated value and tempValue for validation of appropriate inputs
		boolean validated;
		String tempValue;
		
		System.out.print("\n\n__________________________________________________________\n");
		
		int itemLocation = 0;
		do {	//loop if validation ever fails
			validated = true;
			//prompts the user for an input then stores this input into tempValue
			System.out.print(String.format("%-25s %s","Enter Item ID", ""));
			tempValue = input.nextLine().toUpperCase();
			
			//validation relevant to the id when returning an item
			if (validateIdExistance(tempValue) == false) {
				validated = false;
				System.out.println("Error - ID cannot be found in the system");
			}
			else {
				itemLocation = calcItemLocation(tempValue);
				if (validateItemBorrowed(itemLocation) == false) {
					validated = false;
					System.out.println("Error - Item is currently NOT on loan");
				}
			}
		}while (validated == false);	//if any validation failed then it loops and prompts the user for another id
		String id = tempValue;	//after validation variable id holds the input
		
		//prompts the user for amount of days the item was on loan since the borrowDate and places this in daysOnLoan
		System.out.print(String.format("%-25s %s","Enter days on loan", ""));
		int daysOnLoan = input.nextInt();
		input.nextLine();
		
		try {	//if any exception passed through validation
			if (validateIdExistance(id) == false) throw new IdException("could not be found in the system");	//if id does not exist in the system it throws an id exception
			
			itemLocation = calcItemLocation(id);	//itemLocation becomes the value of the index of the id in the array item
			
			DateTime returnDate = calcReturnDate(itemLocation, daysOnLoan);	//calculates the returnDate from the variables
			
			//creates a new item depending on whether its a game or movie using collected variables then prints relevant return details
			if (item[itemLocation] instanceof Game) {
				((Game)item[itemLocation]).returnItem(returnDate);
			}
			else if(item[itemLocation] instanceof Movie) {
				((Movie)item[itemLocation]).returnItem(returnDate);
			}
			System.out.println("The total fee payable is $" + item[itemLocation].getTotalFee() + "\n\n");
			
		}
		catch(BorrowException e) {	//if a borrow exception was found
			System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
		}
		catch(IdException e) {	//if a id exception was found
			System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
		}
	}
	
	public void displayDetails() {	//displays the details of all the items in the item array
		//for every item in the array thats not null, prints the details of the item
		for (int x = 0; x < item.length && item[x] != null; x++) {
			System.out.println("\n__________________________________________________________");
			System.out.println(item[x].getDetails());
		}
	}
	
	public void seedData() {
		//loops through every element in the item array and searches for any element that contains data if there is it makes validated = false
		int counter = 0;
		boolean validated = true;
		while (counter < item.length) {
			if (item[counter] != null) {
				validated = false;
			}
			counter++;
		}
		
		//if validated = false the method ends and returns back to the menu
		if (validated == false) {
			System.out.println("\n\nError - there is currently movies populating the system, seeding will erase these movie\n\n");
			menu();
		}
		
		//adds, borrows, returns items using hard coded data for testing purposes
		String[] platforms = new String[10];
		try {	//to search for any exceptions that can be found when creating hard coded items
			item[0] = new Movie("IRM", "Iron Man", "Action", "Stark builds armour", false);
			item[1] = new Movie("CPA", "Captain America, The first Avenger", "War", "Cap, gets a serum", false);
			((Movie)item[1]).borrowItem("JER", getDate(0));
			item[2] = new Movie("THO", "Thor", "Fantasy", "Thor gets banished", false);
			((Movie)item[2]).borrowItem("KIH", getDate(0));
			((Movie)item[2]).returnItem(calcReturnDate(2, 5));
			item[3] = new Movie("AVG", "The Avengers", "Superhero", "The team unites", false);
			((Movie)item[3]).borrowItem("REN", getDate(0));
			((Movie)item[3]).returnItem(calcReturnDate(3, 10));
			item[4] = new Movie("GOG", "Guardians of the Galaxy", "Space", "Misfits join", false);
			((Movie)item[4]).borrowItem("NAT", getDate(0));
			((Movie)item[4]).returnItem(calcReturnDate(4, 10));
			((Movie)item[4]).borrowItem("JAY", getDate(10));
			item[5] = new Movie("DCS", "Doctor Strange", "Mystery", "Doctor Strange learns the mystic arts", true);
			item[6] = new Movie("ATM", "Ant-Man", "Comedy", "Scott becomes small", true);
			((Movie)item[6]).borrowItem("HIE", getDate(0));
			item[7] = new Movie("SPM", "Spider-Man Homecoming", "Highschool", "Peter tries to prove himself", true);
			((Movie)item[7]).borrowItem("TON", getDate(0));
			((Movie)item[7]).returnItem(calcReturnDate(7, 1));
			item[8] = new Movie("BLP", "Black Panther", "Traditional", "T'Challa fights for the throne", true);
			((Movie)item[8]).borrowItem("TRI", getDate(0));
			((Movie)item[8]).returnItem(calcReturnDate(8, 3));
			item[9] = new Movie("INW", "Infinity War", "Team-up", "Purple guy kills everyone", true);
			((Movie)item[9]).borrowItem("AMY", getDate(0));
			((Movie)item[9]).returnItem(calcReturnDate(9, 3));
			((Movie)item[9]).borrowItem("ROH", getDate(3));
			platforms[0] = "Xbox";
			platforms[1] = "Nintendo DS";
			item[10] = new Game("CUV", "Curious Village", "Investigate the village", "Puzzle", platforms);
			platforms[0] = "PS4";
			platforms[1] = null;
			item[11] = new Game("PAB", "Pandora's Box", "Find the box", "Mystery", platforms);
			((Game)item[11]).borrowItem("CHA", true, getDate(0));
			platforms[0] = "Xbox";
			platforms[1] = "PS4";
			platforms[2] = "Wii";
			item[12] = new Game("LOF", "Lost Future", "What happend", "Drama", platforms);
			((Game)item[12]).borrowItem("BRA", false, getDate(0));
			((Game)item[12]).returnItem(calcReturnDate(12, 19));
			platforms[0] = "Xbox";
			platforms[1] = "Nintendo DS";
			platforms[2] = "PS4";
			platforms[3] = "Wii";
			item[13] = new Game("SPC", "Specter's Call", "Mysterious ghost", "RPG", platforms);
			((Game)item[13]).borrowItem("CHR", true, getDate(0));
			((Game)item[13]).returnItem(calcReturnDate(13, 32));
			
			System.out.println("\n\nSeeded Data\n\n");	//prints to the use that the data was seeded
		}	
		catch(BorrowException e) {	//if a borrow exception is found in the hard coding
			System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
		}
		catch(IdException e) {	//if a id exception is found in the hard coding
			System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
		}
	}
	
	public void exitProgram() {	//method to exit the program and save all data to a file
		PrintWriter outputStream = null;
		System.out.println("\n\nExiting Program...");	//tell the user the system is exiting

		try {	//searches if there are any exceptions that occurs
			outputStream = new PrintWriter(new FileOutputStream(fileName));	//outputStream becomes the writer to the the file
			
			//goes through each item element and prints the relevant details into the file
			int counter = 0;
			while(counter < item.length) {
				if (item[counter] instanceof Game) {
					outputStream.write(((Game)item[counter]).toString() + "\r\n");
				}
				else if(item[counter] instanceof Movie) {
					outputStream.write(((Movie)item[counter]).toString() + "\r\n");
				}
				counter++;
			}
		}
		catch(FileNotFoundException e){	//if the file could not be found then
			System.out.println(e);	//prints an error
			System.exit(0);	//exits the system
		}
		outputStream.close();	//closes the output stream to the file
		//section of code to write to the backup record
		
		//repeats previous code but writes it to a backup file
		try {	//searches if there are any exceptions that occurs
			outputStream = new PrintWriter(new FileOutputStream("record_backup.txt"));	//outputStream becomes the writer to the the file
			
			//goes through each item element and prints the relevant details into the file
			int counter = 0;
			while(counter < item.length) {
				if (item[counter] instanceof Game) {
					outputStream.write(((Game)item[counter]).toString() + "\r\n");
				}
				else if(item[counter] instanceof Movie) {
					outputStream.write(((Movie)item[counter]).toString() + "\r\n");
				} 
				counter++;
			}
		}
		catch(FileNotFoundException e){	//if the file could not be found then
			System.out.println(e);	//prints an error
			System.exit(0);	//exits the system
		}
		outputStream.close();	//closes the output stream to the file
		//section of code to write to the backup record
		
		//exits the program
		System.out.println("Program exited");
		System.exit(0);
	}
	
	
	public void fileRead() {	//method to read from the file
		Scanner inputStream = null;
		File file = new File(fileName);
		
		//if a file doesn't exist to read from it creates a file
		if (file.exists() == false) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error - can't create new file");
			}
		}
		
		//opens the file to read from
		try {
			inputStream = new Scanner(new File(fileName));
		}
		catch(FileNotFoundException e){
			System.out.println("Error opening the file " + fileName);
			System.exit(0);	//exits the system
		}
		
		//reads each line that has data
		while(inputStream.hasNextLine()) {
			//reads the line and splits the data into an array splitInput to easily access individual data
			String bufferReader = inputStream.nextLine();
			String[] splitInput = bufferReader.split(":");
			
			int hireRecordAmount = 0;	//declare hireRecordAmount to hold amount of hireRecords in the line
			
			if (splitInput[0].substring(0, 1).equals("M")) {	//if the line is for a movie
				//finds if the movie is a new release
				boolean isNewRelease;
				if (splitInput[4].equals("WK")) isNewRelease = true;
				else isNewRelease = false;
				
				hireRecordAmount = (splitInput.length - 6) / 5;	//calculates amount of hireRecords in the line
				
				//creates the movie
				try {	//finds if there is an exception when creating the movie
					item[itemCounter] = new Movie(splitInput[0].substring(2, 5), splitInput[1], splitInput[3], splitInput[2], isNewRelease);	//creates the movie based on obtained variables from the file
				}
				catch(IdException e) {	//if an exception is found when creating the movie
					System.out.println("\n" + e.getMessage() + "\n\n");	//it prints an error
				}
				
				//borrows and or returns the movie for each hireRecordAmount
				for (int x = 0; x < hireRecordAmount; x++) {
					try {	//searches for any exceptions
						((Movie)item[itemCounter]).borrowItem(splitInput[splitInput.length - 5 - (x * 5)].substring(6, 9), getDate(getDate(0).diffDays(
								getSetDate(
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(8, 10)),
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(5, 7)),
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(0, 4))), 
								getDate(0)) + 1));	//borrows the movie at the date read from the record
						
						if(splitInput[splitInput.length - 1 - (x * 5)].equals("null") == false) {	//if the record is returned
							((Movie)item[itemCounter]).returnItem(getDate(getDate(0).diffDays(
									getSetDate(
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 5)].substring(8, 10)),
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 5)].substring(5, 7)),
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 5)].substring(0, 4))),
									getSetDate(
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(8, 10)),
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(5, 7)),
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 5)].substring(0, 4))))- 
									getDate(0).diffDays(getDate(0), 
											getSetDate(
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(8, 10)),
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(5, 7)),
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(0, 4)))) + 1));	//returns the movie at the read return date
						}
					}
					catch(BorrowException e) {	//if a borrow exception is found
						System.out.println("\n" + e.getMessage() + "\n\n");	//it prints an error
					}
				}
			}
			else if(splitInput[0].substring(0, 1).equals("G")) {	//if the line is for a game
				hireRecordAmount = (splitInput.length - 6) / 6;	//hireRecordAmount becomes the amount of hire records on the line
				
				String[] platforms = splitInput[4].split(", ");	//declare array of platforms which is split from splitInput at index 4
				
				//creates a new game based on variables collected
				try {	//searches for any exceptions to be found
					item[itemCounter] = new Game(splitInput[0].substring(2, 5), splitInput[1], splitInput[3], splitInput[2], platforms);	//creates a new game
				}
				catch(IdException e) {	//if a borrow exception is found then
					System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
				}
				
				//borrows and or returns the game based on the amount hireRecordAmount
				for (int x = 0; x < hireRecordAmount; x++) {
					//finds if the hireRecord was extended
					boolean extended;
					if (splitInput[splitInput.length - 3 - (x * 6)].equals("true")) extended = true;
					else extended = false;
					
					try {	//searches for an exception
						((Game)item[itemCounter]).borrowItem(splitInput[splitInput.length - 6 - (x * 6)].substring(6, 9), extended, getDate(getDate(0).diffDays(
								getSetDate(
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(8, 10)),
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(5, 7)),
										Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(0, 4))), 
								getDate(0)) + 1));	//borrows the movie at the borrowDate in the file
						
						if(splitInput[splitInput.length - 1 - (x * 5)].equals("null") == false) {	//if the movie has been returned
							((Game)item[itemCounter]).returnItem(getDate(getDate(0).diffDays(
									getSetDate(
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 6)].substring(8, 10)),
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 6)].substring(5, 7)),
											Integer.parseInt(splitInput[splitInput.length - 1 - (x * 6)].substring(0, 4))),
									getSetDate(
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(8, 10)),
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(5, 7)),
											Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(0, 4)))) - 
									getDate(0).diffDays(getDate(0), 
											getSetDate(
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(8, 10)),
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(5, 7)),
													Integer.parseInt(splitInput[splitInput.length - 2 - (x * 6)].substring(0, 4)))) + 1));	//returns the movie at the specified return date in the file
						}
					}
					catch(BorrowException e) {	//if a borrow exception is found then
						System.out.println("\n" + e.getMessage() + "\n\n");	//prints an error
					}
				}
			}
			itemCounter++;	//after each item added itemCounter adds one so that it moves to the next position in the item array
		}
	}
	
	
	public DateTime getDate(int daysAhead) {	//returns variable DateTime with the value of days forward from the current date
		DateTime date = new DateTime(daysAhead);
		return date;	//returns DateTime variable
	}
	
	public DateTime getSetDate(int day, int month, int year) {	//returns variable DateTime with the value set to given inputs day, month, year
		DateTime date = new DateTime(day, month, year);
		return date;	//returns DateTime variable
	}
	
	public DateTime calcReturnDate(int itemLocation, int daysOnLoan) {	//calculates and returns the returnDate by an input daysOnLoan after the borrowDate
		//calculates the DateTime of the returnDate through the difference between daysOnLoan and difference between currentDate and BorrowDate
		return getDate(daysOnLoan - getDate(0).diffDays(getDate(0), item[itemLocation].getBorrowDate()));
	}
	
	
	public int calcItemLocation(String tempValue) {	//searches through all item IDs comparing and returns the value of the array location of the array matching tempValue
		if (validateIdExistance(tempValue)){	//validation if ID is actually in the system
			for(int x = 0; x < item.length && item[x] != null; x++) {	//for loop which loops until its gone through each array or encounters an empty array
				if (item[x].getId().substring(2).equals(tempValue)) {	//compares the ID of the searched item and compares it to tempValue and returns itemLocation
					return x;
				}
			}
			return 0;	//if nothing is found it returns 0 (should never happen)
		}
		else {	//if id is not in the system, it prints an error
			System.out.println("Error - item not found");
			return 0;
		}
	}
	
	public boolean validateItemBorrowed(int itemLocation) {	//validation to check whether item is borrowed or not
		if (item[itemLocation].getCurrentlyBorrowed() == null) return false;	//if the item given a location in the array is not borrowed method returns false
		else return true;
	}
	
	public boolean validateIdExistance(String tempValue) {	//validation to check if an input ID is in the system
		boolean found = false;
		for(int x = 0; x < item.length && item[x] != null; x++) {	//loop which continues until all items are searched or the loop encounters a null item
			if (item[x].getId().substring(2).equals(tempValue)) {	//for every iteration of the loop the item ID is obtained and compared to tempValue
				found = true;
			}
		}
		if (found == true) return true;	//after it checks everything an if statement looks if any item has been found to equal tempValue and returns true if it has
		else return false;
	}
	
	public boolean validateIdLength(String tempValue) {	//validate the length of ID input
		if (tempValue.length() != 3) {	//if the tempValue does not have 3 characters it returns false
			System.out.println("Error - ID must be 3 characters long\n");
			return false;
		}
		else return true;
	}
	
	public String validateChoice(String choiceA, String choiceB, String enterWhat) {	//method to validate user input for a 2 choice answer
		//Declare boolean validated value and tempValue for validation of appropriate inputs
		boolean validated;
		String tempValue;
		
		do {	//do while loop that continues to loop until there is a valid input
			validated = true;
			
			//prompts the user for an input and it stores that input in tempValue
			System.out.print(enterWhat);
			tempValue = input.nextLine().toUpperCase();
			
			//compares tempValue to the choices and returns the choice which tempValue equals to or else prompts the user gain
			if(tempValue.equals(choiceA)) return choiceA;
			else if(tempValue.equals(choiceB)) return choiceB;
			else {
				System.out.println("Error - please select an option of the following\n");
				validated = false;
			}
		}while (validated == false);
		return "Error - validation failed\n";	//if this return ever occurs there is an error as it skipped validation
	}
}
