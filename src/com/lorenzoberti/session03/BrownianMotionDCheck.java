/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
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
		BrownianMotionInterfaceEnhanced brownian;
		
		System.out.println("Single Brownian motion check:");
		// check the average and the variance

		
		// check the covariance of 1 Brownian motion at two different times
		
		
		System.out.println();

		// check the brownian increment (they should be stationary and independent)
		

		System.out.println("Multidimensional Brownian motion check:");
		// check the independence between factors (Covariance test)
		

		
		
	}

}
