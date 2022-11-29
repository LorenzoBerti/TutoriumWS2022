/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.stochastic.RandomVariable;

/**
 * This class compute the confidence interval for the mean of a random variable
 * by using the Chebyschev inequality.
 * 
 * @author Lorenzo Berti
 *
 */
public class ChebyshevInterval implements ConfidenceInterval{
	
	RandomVariable randomVariable;
	
	

	public ChebyshevInterval(RandomVariable randomVariable) {
		
		this.randomVariable = randomVariable;
	}



	@Override
	public double[] getConfidenceInterval(int numberOfSimulations, double level) {
		
		double mean = randomVariable.getAverage();
		double stdDeviation = Math.sqrt(randomVariable.getVariance());
		
		return new double[] {(mean-stdDeviation/Math.sqrt(numberOfSimulations*level)), mean+stdDeviation/Math.sqrt(numberOfSimulations*level)};
	}

}