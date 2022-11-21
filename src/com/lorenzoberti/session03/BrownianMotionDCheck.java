/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class checks if your implementation of the interface
 * BrownianMotionInterfaceEnhanced generates a "good" Brownian motion. Have a look to the
 * methods of the Interface RandomVariable.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionDCheck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfFactors = 2; // 2-dimensional Brownian motion
		int numberOfPaths = 100000;

		double initialTime = 0.0;
		double finalTime = 10.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		// Constructor
		BrownianMotionInterfaceEnhanced brownian = new BrownianMotionEnhanced(times, numberOfFactors, numberOfPaths);
		
		double time = 10.0;
		RandomVariable brownianAtTime = brownian.getBrownianMotionAtSpecificTime(0, time);
		
		System.out.println("Single Brownian motion check:");
		// check the average and the variance
		double average = brownianAtTime.getAverage();
		double variance = brownianAtTime.getVariance();
		System.out.println("Average at time "+time+" is...: "+average);
		System.out.println("Variance at time "+time+" is..: "+variance);
		
		// check the covariance of 1 Brownian motion at two different times
		double otherTime = 8.0;
		RandomVariable brownianAtOtherTime = brownian.getBrownianMotionAtSpecificTime(0, otherTime);
		double covariance = brownianAtTime.covariance(brownianAtOtherTime).getAverage();
		System.out.println("The covariance between time "+time+" and time "+otherTime+" is..: "+covariance);
		
		System.out.println();

		// check the brownian increment (they should be stationary and independent)
		RandomVariable brownianIncrementAtTime = brownian.getBrownianIncrement(0, 8);
		RandomVariable brownianIncrementAtOtherTime = brownian.getBrownianIncrement(0, 5);
		double averageIncrement = brownianIncrementAtTime.getAverage();
		double varianceIncrement = brownianIncrementAtTime.getVariance();
		double covarianceIncrement = brownianIncrementAtTime.covariance(brownianIncrementAtOtherTime).getAverage();
		System.out.println("Increment Brownian motion check:");
		System.out.println("Average of the increment........: "+averageIncrement);
		System.out.println("Variance of the increment.......: "+varianceIncrement);
		System.out.println("Covariance between increments...: "+covarianceIncrement);
		
		System.out.println();
		
		System.out.println("Multidimensional Brownian motion check:");
		// check the independence between factors (Covariance test)
		RandomVariable brownianAtTimeFactor1 = brownian.getBrownianMotionAtSpecificTime(1, time);
		double covarianceFactors = brownianAtTimeFactor1.covariance(brownianAtTime).getAverage();
		System.out.println("Covariance between factors...: "+covarianceFactors);
		
		
	}

}