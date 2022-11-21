/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.functions.NormalDistribution;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.randomnumbers.MersenneTwister;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionEnhanced implements BrownianMotionInterfaceEnhanced {

	private final TimeDiscretization times;

	private final int numberOfFactors;
	private final int numberOfPaths;
	
	private int seed;
	
	// Random Number generator
	private MersenneTwister mersenne = new MersenneTwister(seed);

	// two-dimension array storing the Brownian increments and the Brownian paths
	private RandomVariable[][] brownianIncrements;
	private RandomVariable[][] brownianPaths;

	public BrownianMotionEnhanced(TimeDiscretization times, int numberOfFactors, int numberOfPaths) {
		this.times = times;
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
		this.seed = 3031;
	}
	
	// Overloading constructor! Just another constructor for the same object...
	public BrownianMotionEnhanced(TimeDiscretization times, int numberOfFactors, int numberOfPaths, int seed) {
		this.times = times;
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
		this.seed = seed;
	}
	

	
	@Override
	public TimeDiscretization getTimeDiscretization() {

		return times;
	}
	
	@Override
	public int getNumberOfFactors() {
		
		return numberOfFactors;
	}
	
	@Override
	public RandomVariable[][] getAllPaths() {
		if (brownianPaths == null) {
			generate();
		}

		RandomVariable[][] brownianPathsClone = brownianPaths.clone();
		return brownianPathsClone;
	
	}

	@Override
	public RandomVariable getBrownianMotionAtSpecificTime(int indexFactor, double time) {

		RandomVariable[][] allPaths = getAllPaths();

		return allPaths[times.getTimeIndex(time)][indexFactor];
	}

	@Override
	public RandomVariable getBrownianMotionAtSpecificTimeIndex(int indexFactor, int timeIndex) {

		RandomVariable[][] allPaths = getAllPaths();

		return allPaths[timeIndex][indexFactor];
	}

	@Override
	public RandomVariable[] getPathsForFactor(int indexFactor) {

		RandomVariable[][] allPaths = getAllPaths();
		int numberOfTimes = times.getNumberOfTimes();
		RandomVariable[] paths = new RandomVariableFromDoubleArray[numberOfTimes];

		for (int i = 0; i < numberOfTimes; i++) {
			paths[i] = allPaths[i][indexFactor];
		}
		
		return paths;
	}

	@Override
	public double[] getSpecificPathForFactor(int indexFactor, int pathIndex) {

		RandomVariable[] path = getPathsForFactor(indexFactor);
		int numberOfTimes = path.length;
		double[] specificPath = new double[numberOfTimes];
		for (int i = 0; i < numberOfTimes; i++) {
			specificPath[i] = path[i].get(pathIndex);
		}

		return specificPath;
	}

	@Override
	public RandomVariable[][] getBrownianIncrements() {

		if (brownianIncrements == null) {
			generate();
		}
		
		return brownianIncrements.clone();
	}

	@Override
	public RandomVariable getBrownianIncrement(int indexFactor, int timeIndex) {

		RandomVariable[][] allIncrements = getBrownianIncrements();

		return allIncrements[timeIndex][indexFactor];
	}

	private void generate() {

		// Todo

}
	



}