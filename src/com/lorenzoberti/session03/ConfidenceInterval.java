/**
 * 
 */
package com.lorenzoberti.session03;

/**
 * This interface represents a general confidence interval for parameter
 * estimation of a random variable.
 * 
 * @author Lorenzo Berti
 *
 */
public interface ConfidenceInterval {

	/**
	 * It returns the confidence interval at 1-level.
	 * 
	 * @param level
	 * @return the confidence interval at 1-level.
	 */
	double[] getConfidenceInterval(int numberOfSimulations, double level);

}