package com.skilldistillery.foodtruck.app;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.IntBinaryOperator;

import com.skilldistillery.foodtruck.entities.FoodTruck;

public class FoodTruckApp {

	private FoodTruck[] foodTrucks;
	private int truckIndex;
	private Scanner scan;
	private boolean userQuitsOrMaxTrucks;
	private boolean userQuitsOptionsMenu;

	public static void main(String[] args) {
		FoodTruckApp app = new FoodTruckApp();
		app.go();
	}

	public FoodTruckApp() {
		foodTrucks = new FoodTruck[5];
		scan = new Scanner(System.in);
	}

	public void go() {
		System.out.println("Welcome to Food Truck App!\n");

		/*
		 * When the getUserInput() loop returns, truckIndex must equal the
		 * number of trucks entered.
		 */
		do {
			getUserInput();
		} while (!userQuitsOrMaxTrucks);

		// edge case
		if (truckIndex == 0) {
			System.out.println("Looks like you didn't have any trucks to enter today.");
			System.out.println("Thank you for using Food Truck App! Goodbye.");
			return;
		}

		// enable simpler processing by truncating the array
		foodTrucks = Arrays.copyOf(foodTrucks, truckIndex);

		do {
			optionsMenu();
		} while (!userQuitsOptionsMenu);

		System.out.println("Thank you for using Food Truck App! Goodbye.");
		scan.close();
	}

	public void getUserInput() {

		// get name or quit
		System.out.print("Please enter the food truck name, or [quit] to stop: ");
		String name = scan.nextLine();
		
		if (name.toLowerCase().equals("quit") || name.equals("[quit]")) {
			userQuitsOrMaxTrucks = true;
			return;
		}

		// get type
		System.out.print("Please enter the food type for this food truck: ");
		String type = scan.nextLine();

		// get rating, and make sure it's a valid rating.
		int rating;
		boolean ratingOK = false;
		do {
			System.out.print("Please enter the a 1 to 5 star rating for this truck: ");

			rating = scan.nextInt();
			scan.nextLine(); // flush

			ratingOK = (rating >= 1) && (rating <= 5);
			if (!ratingOK) {
				System.out.println("Oops! We didn't understand that response.");
			}
		} while (!ratingOK);

		// make some space between entries
		System.out.println();

		// park the truck into the array
		foodTrucks[truckIndex++] = new FoodTruck(name, type, rating);
		
		if (truckIndex == 5) {
			// this is the last truck we can accept
			userQuitsOrMaxTrucks = true;
		}

	}

	public void optionsMenu() {
		System.out.println("\n~~ Please Enter a Number to Choose an Option ~~");
		System.out.println("1) List All Existing Food Trucks");
		System.out.println("2) See the Average Rating of the Food Trucks");
		System.out.println("3) Display the Highest-Rated Food Truck(s)");
		System.out.println("4) Quit the Program");

		int answer = scan.nextInt();
		scan.nextLine(); // flush
		
		dispatchOnAnswer(answer);
	}

	public void dispatchOnAnswer(int choice) {
		switch (choice) {
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
			userQuitsOptionsMenu = true;
			break;
		default:
			System.out.println("Oops! We didn't understand that response.");
			optionsMenu(); // try again
		}
	}

	/* 
	 * Here be lambdas
	 */
	
	// for finding both the sum and the max of truck ratings
	public int reduceTruckRatingsWith(IntBinaryOperator f){
	    return Arrays.stream(foodTrucks)
	    		     .map(t -> t.getRating())
	    		     .reduce(0, (x, y) -> f.applyAsInt(x, y));
	}
	
	public void averageTrucks() {
		System.out.println("~~ The Average Food Truck Rating ~~");

		int sum = reduceTruckRatingsWith((x, y) -> x + y);
		double avg = ((double) sum) / truckIndex;
		
		System.out.printf("\t  ~~ %.1f stars ~~%n", avg);
	}

	// displays multiple trucks when there is a tie for top rated
	public void displayBestTrucks() {
		System.out.println("~~ Top Rated Food Truck(s) ~~");

		int top = reduceTruckRatingsWith((x, y) -> Math.max(x, y));

		for (FoodTruck truck : foodTrucks) {
			if (truck.getRating() == top) {
				System.out.println(truck);
			}
		}
	}
	
	public void listTrucks() {
		System.out.println("~~ All Entered Food Trucks ~~");
		Arrays.stream(foodTrucks).forEach(x -> System.out.println(x));
	}
}
