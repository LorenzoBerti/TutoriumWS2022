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
		super();
		this.numberOfPaths = numberOfPaths;
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.timeStep = timeStep;
	}

	
	@Override
	public double[][] getPaths() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public double[] getProcessAtTimeIndex(int timeIndex) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public double[] getSpecificPath(int path) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public double getSpecificValue(int path, int timeIndex) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	// Private! The user does not need this method: it is used only inside the class to create the Brownian motion
	private void generate() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
