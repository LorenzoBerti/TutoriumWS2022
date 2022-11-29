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

		final int numberOfTimeSteps = times.getNumberOfTimeSteps();
		// numberOfTimes = numberOfTimeSteps + 1
		final int numberOfTimes = times.getNumberOfTimes();
		
		final double[][][] brownianIncrementsArray = new double[numberOfTimeSteps][numberOfFactors][numberOfPaths];
		final double[][][] brownianPathsArray = new double[numberOfTimes][numberOfFactors][numberOfPaths];
		
		for(int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			for(int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
				
				brownianPathsArray[0][factorIndex][pathIndex] = 0.0;
				
				for(int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
					
					double random = mersenne.nextDouble();
					brownianIncrementsArray[timeIndex][factorIndex][pathIndex] = NormalDistribution
							.inverseCumulativeDistribution(random) * Math.sqrt(times.getTimeStep(timeIndex)); // W(t_{i+1})-W(t_i)
					brownianPathsArray[timeIndex+1][factorIndex][pathIndex] = brownianPathsArray[timeIndex][factorIndex][pathIndex] 
							+ brownianIncrementsArray[timeIndex][factorIndex][pathIndex]; // W(t_{i+1}) = W(t_i) + W(t_{i+1})-W(t_i)
				}
				
			}
		}
		
		brownianIncrements = new RandomVariable[numberOfTimeSteps][numberOfFactors];
		brownianPaths = new RandomVariable[numberOfTimeSteps + 1][numberOfFactors];
		
		for(int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
			
			brownianPaths[0][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(0), 0);
			
			for(int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
				
				brownianIncrements[timeIndex][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(timeIndex), 
						brownianIncrementsArray[timeIndex][factorIndex]);
				brownianPaths[timeIndex+1][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(timeIndex +1),
						brownianPathsArray[timeIndex+1][factorIndex]);
				
			}
		}
		
		
		
		

}
	



}