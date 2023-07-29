# FoodTruckProject

## Overview

A "Food Truck App" program. A user first inputs food truck data. Then the user may choose to have some simple data operations performed. Example interaction:

~~~
~~ Please Enter a Number to Choose an Option ~~
1) List All Existing Food Trucks
2) See the Average Rating of the Food Trucks
3) Display the Highest-Rated Food Truck(s)
4) Quit the Program
2                                              // user input for average
~~ The Average Food Truck Rating ~~
	  ~~ 2.7 stars ~~

~~ Please Enter a Number to Choose an Option ~~
1) List All Existing Food Trucks
2) See the Average Rating of the Food Trucks
3) Display the Highest-Rated Food Truck(s)
4) Quit the Program
3                                              // user input for highest rated
~~ Top Rated Food Truck(s) ~~
Truck ID: 0 | Name: Tacos On Wheels
Rating:   4 | Type: Taco Truck
~~~


## Technologies Used

- Eclipse
- Java
- Git
- Github

## Lessons Learned

The simple data processing tasks like "average" and "sum" over collections can be straight-forwardly calculated using Java 8's lambdas and streams. The code in this project uses:

~~~
public int topRating(FoodTruck[] trucks) {
	return mapReduceWith(trucks, (x, y) -> Math.max(x, y));
}

public double averageRating(FoodTruck[] trucks) {
	int sum = mapReduceWith(trucks, (x, y) -> x + y);
	return ((double) sum) / numberOfTrucks;
}

public int mapReduceWith(FoodTruck[] trucks, IntBinaryOperator op){
    return Arrays.stream(trucks)
    		     .map(truck -> truck.getRating())
    		     .reduce(0, (x, y) -> op.applyAsInt(x, y));
}
~~~

What's interesting about Java's lambdas, is that their types don't work like I expected they might. The usual typing rules for lambda terms look something like this:

~~~
theTerm = λ (int x) { λ (int y) { x + y }}

theType = int ⟶ (int ⟶ int)
~~~

Instead, a lambda term in Java is typed with a "good old fashion Java interface". For example, the IntBinaryOperator interface has a definition akin to:
 
 ~~~
public interface IntBinaryOperator {
    public int op(int a, int b);
}
 ~~~

 In all honesty, it was a stumbling block for me to learn the "interface way" to do things. And I wondered, "Why is it like this? Why didn't they just add the usual lambda typing rules to Java when they added lambda?"

 Well, it turns out there are many good reasons for that. 

 In fact, in a [2013 GOTO conference talk](https://youtu.be/MLksirK9nnE), Brian Göetz, a Java Language Architect who works on the JVM, addressed exactly this issue in detail:

> We could teach the VM about function types, but this would be a huge effort. It would effect signatures. It would effect bytecodes. It would effect verification rules. \[...\] The takeaway point is: "Just add function types" was obvious... and wrong. It would have been complex. It would have introduced corner cases. It would have exposed users to more of the pain of erasure, which isn't nice. It also would have had the effect of completely bifurcating the world of Java libraries. \[...\] We don't want to create this split between old and new libraries. By using an old mechanism \[interfaces\], we avoid that problem. And as a bonus, existing libraries that were designed years before lambda, all of a sudden are compatible with lambda as long as they are using this pattern...



So, what did I learn? 

I learned some things about how to utilize lambdas in Java, sure. But I feel the bigger lesson is that language design decisions that seem strange at first, probably have very good reasons for existing. It just takes a little time and research to find out why.



