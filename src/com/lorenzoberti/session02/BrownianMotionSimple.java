/**
 * 
 */
package com.lorenzoberti.session02;

import java.util.function.DoubleUnaryOperator;

import net.finmath.functions.NormalDistribution;
import net.finmath.plots.Plot2D;
import net.finmath.randomnumbers.MersenneTwister;

/**
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionSimple implements BrownianMotionInterface{

	// Field of the class. Note: they are private, meaning that they cannot be accessed outside this class
	private int numberOfPaths; // the number of paths that you want to simulate
	private int numberOfTimeSteps; // the number of time steps that you want to use
	private double timeStep;  // the length of the time step
	
	private double wholePaths[][]; // a 2-dimensional array, namely a matrix, which will store all the realizations of the BM

	// Constructor: this is used to construct the object when used. 
	//Here an example of a possible constructor. Feel free to modify it if you prefer another implementation...
	public BrownianMotionSimple(int numberOfPaths, int numberOfTimeSteps, double timeStep) {
		this.numberOfPaths = numberOfPaths;
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.timeStep = timeStep;
	}

	
	@Override
	public double[][] getPaths() {
		// lazy inizialization: we inizialize it only one time
		// but this is would not be a problem because everything is random
		if (wholePaths == null) {
			generate();
		}
		return wholePaths;
	}

	@Override
	public double[] getProcessAtTimeIndex(int timeIndex) {
		if (wholePaths == null) {
			generate();
		}
		double[] processAtGivenTime = new double[numberOfPaths];

		for (int i = 0; i < numberOfPaths; i++) {
			processAtGivenTime[i] = wholePaths[i][timeIndex];
		}

		return processAtGivenTime;
	}

	@Override
	public double[] getSpecificPath(int path) {
		if (wholePaths == null) {
			generate();
		}
		double[] specificPath = new double[numberOfTimeSteps + 1];
		
		for (int i = 0; i < numberOfTimeSteps + 1; i++) {
			specificPath[i] = wholePaths[path][i];
		}
			
		return wholePaths[path];
	}

	@Override
	public double getSpecificValueAtTimeIndex(int path, int timeIndex) {
		if (wholePaths == null) {
			generate();
		}
		return wholePaths[path][timeIndex];
	}

	
	// Private! The user does not need this method: it is inside the class
	private void generate() {

		wholePaths = new double[numberOfPaths][numberOfTimeSteps + 1];
		
		// Java initialize the array with values = 0, so we do not need to initialize the Brownian motion at 0 by hand
//		for(int i = 0; i < numberOfPaths; i++) {
//			wholePaths[i][0] = 0;
//		}
		
		// Random number generator
		MersenneTwister mersenne = new MersenneTwister(3081);
		
		// now we generate the Brownian motion path by path
		for(int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			for(int timeIndex = 1; timeIndex < numberOfTimeSteps+1; timeIndex++) {
				// we have to generate the value drawing from the Normal distribution:
				double random = mersenne.nextDouble(); // draw from uniform [0,1]
				//double random = Math.random();
				double normal = NormalDistribution.inverseCumulativeDistribution(random); // inverse cdf
				wholePaths[pathIndex][timeIndex] = wholePaths[pathIndex][timeIndex-1] + Math.sqrt(timeStep)*normal; // Brownian motion
				
			}
		}
		

	}

	@Override
	public void printPath(int path) {

		DoubleUnaryOperator trajectory = t -> {
			return (getSpecificValueAtTimeIndex(path, (int) t));
		};

		Plot2D plot = new Plot2D(0, numberOfTimeSteps, numberOfTimeSteps +1, trajectory);
		plot.setTitle("Brownian motion path");
		plot.setXAxisLabel("Time");
		plot.setYAxisLabel("Brownian motion");
		plot.show();

	}


	

	
	
}
