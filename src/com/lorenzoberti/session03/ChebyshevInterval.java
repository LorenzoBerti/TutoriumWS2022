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
		
		// Todo
		return null;
	}

}