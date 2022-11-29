/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.functions.NormalDistribution;
import net.finmath.stochastic.RandomVariable;

/**
 * This class compute the confidence interval for the mean of a random variable
 * by using the Central Limit Theorem.
 * 
 * @author Lorenzo Berti
 *
 */
public class NormalInterval implements ConfidenceInterval{
	
	RandomVariable randomVariable;
	
	

	public NormalInterval(RandomVariable randomVariable) {
		
		this.randomVariable = randomVariable;
	}



	@Override
	public double[] getConfidenceInterval(int numberOfSimulations, double level) {
		
		double quantile = NormalDistribution.inverseCumulativeDistribution((1-level)/2);
		double mean = randomVariable.getAverage();
		double stdDeviation = Math.sqrt(randomVariable.getVariance());
		
		return new double[] { mean - quantile*stdDeviation/Math.sqrt(numberOfSimulations), 
				mean + quantile*stdDeviation/Math.sqrt(numberOfSimulations)};
		}
	}

