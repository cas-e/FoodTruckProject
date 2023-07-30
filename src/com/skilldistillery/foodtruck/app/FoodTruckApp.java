package com.skilldistillery.foodtruck.app;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BooleanSupplier;
import java.util.function.IntBinaryOperator;

import com.skilldistillery.foodtruck.entities.FoodTruck;

public class FoodTruckApp {

	private FoodTruck[] foodTrucks; // updated in getInput loop
	private int numberOfTrucks;     // updated in getInput loop
	private int truckRating;        // updated in getValidRating loop
	
	private Scanner scan;
	
	private final boolean keepLooping = true;  // help to remember signals in looper methods
	private final boolean doneLooping = false;

	
	public static void main(String[] args) {
		FoodTruckApp app = new FoodTruckApp();
		app.run();
	}

	private FoodTruckApp() {
		foodTrucks = new FoodTruck[5];
		scan = new Scanner(System.in);
	}

	private void run() {
		System.out.println("Welcome to Food Truck App!\n");

		userLoop(this::getInput);

		if (numberOfTrucks == 0) { // edge case
			System.out.println("Looks like you didn't have any trucks to enter today.");
			System.out.println("Thank you for using Food Truck App! Goodbye.");
			return;
		}
		
		// this array truncation is assumed by the processing methods later
		// it is essential for the correctness of those methods
		foodTrucks = Arrays.copyOf(foodTrucks, numberOfTrucks);

		userLoop(this::doMenu);

		System.out.println("Thank you for using Food Truck App! Goodbye.");
		scan.close();
	}
	


	/*
	 * getInput, getValidRating, and doMenu are all looper methods.
	 * They return either doneLooping or keepLooping to the userLoop factory
	 */
	
	// factory for making looping constructs
	private void userLoop(BooleanSupplier looper) {
		boolean moreLoops;
		do {
			moreLoops = looper.getAsBoolean();
		} while (moreLoops);
	}
	
	private boolean getInput() {
		System.out.print("Please enter the food truck name, or [quit] to stop: ");
		String name = scan.nextLine();
		
		if (name.toLowerCase().equals("quit") || name.equals("[quit]")) {
			return doneLooping;
		}

		System.out.print("Please enter the food type for this food truck: ");
		String type = scan.nextLine();

		
		userLoop(this::getValidRating); // we only accept 1 thru 5 star ratings
		
		System.out.println();           // make some space between entries

		foodTrucks[numberOfTrucks++] = new FoodTruck(name, type, truckRating); 
		
		if (numberOfTrucks == 5) {
			System.out.println("Looks like our food truck storage space is full.");
			return doneLooping;
		}
		
		return keepLooping;
	}

	private boolean getValidRating() {
		System.out.println("Please enter a star rating for this truck: ");
		System.out.print("Was it 1, 2, 3, 4, or 5 stars? ");

		truckRating = scan.nextInt();
		scan.nextLine();              // flush

		boolean ratingOK = (truckRating >= 1) && (truckRating <= 5);
		if (!ratingOK) {
			System.out.println("Oops! We didn't understand that response.");
			return keepLooping;
		}
		return doneLooping;
	}
	
	private boolean doMenu() {
		System.out.println("\n~~ Please Enter a Number to Choose an Option ~~");
		System.out.println("1) List All Existing Food Trucks");
		System.out.println("2) See the Average Rating of the Food Trucks");
		System.out.println("3) Display the Highest-Rated Food Truck(s)");
		System.out.println("4) Quit the Program");

		int answer = scan.nextInt();
		scan.nextLine();             // flush
		
		switch (answer) {
		case 1:
			listTrucks();
			break;
		case 2:
			averageTrucks();
			break;
		case 3:
			displayBestTrucks();
			break;
		case 4:
			return doneLooping;
		default:
			System.out.println("Oops! We didn't understand that response.");
		}
		return keepLooping;
	}


	/*
	 * Data processing and display methods
	 */
	
	private void listTrucks() {
		System.out.println("~~ All Entered Food Trucks ~~");
		Arrays.stream(foodTrucks).forEach(x -> System.out.println(x));
	}
	
	private void averageTrucks() {
		System.out.println("~~ The Average Food Truck Rating ~~");

		int sum = reduceTruckRatingsWith((x, y) -> x + y);
		double avg = ((double) sum) / numberOfTrucks;
		
		System.out.printf("\t  ~~ %.1f stars ~~%n", avg);
	}

	// displays multiple trucks when there is a tie for top rated
	private void displayBestTrucks() {
		System.out.println("~~ Top Rated Food Truck(s) ~~");

		int top = reduceTruckRatingsWith((x, y) -> Math.max(x, y));

		for (FoodTruck truck : foodTrucks) {
			if (truck.getRating() == top) {
				System.out.println(truck);
			}
		}
	}
	
	// for finding both the sum and the max of truck ratings
	private int reduceTruckRatingsWith(IntBinaryOperator f){
	    return Arrays.stream(foodTrucks)
	    		     .map(truck -> truck.getRating())
	    		     .reduce(0, (x, y) -> f.applyAsInt(x, y));
	}
	

}
