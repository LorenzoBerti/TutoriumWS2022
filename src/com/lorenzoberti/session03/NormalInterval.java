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
		
		// Todo
		return null;
	}

}