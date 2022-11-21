/**
 * 
 */
package com.lorenzoberti.session04;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session03.BrownianMotionEnhanced;
import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to check if your implementation of the ProcessSimulator is good. You can use the
 * private method getAssetAtSpecific time to see if your implementation are good
 * enough. Note: the method getAssetAtSpecific generates directly and only the
 * process at the given time and thus not the entire path!
 * 
 * @author Lorenzo Berti
 *
 */
public class ProcessTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionInterfaceEnhanced brownian = new BrownianMotionEnhanced(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double mu = 0.1;
		double sigma = 0.2;

		// Take last value of your simulated processes


		// Take the average...


		// ...and print
		System.out.println("The Euler scheme average is...............: " );
		System.out.println("The average with the private method is....: " );

		// Take the variance...


		// ...and print
		System.out.println("The Euler scheme variance is..............: " );
		System.out.println("The variance with the private method is...: " );
		
		// Compute the analytic mean and analytic variance of the geometric brownian motion
		




	}

	// Private method that generate the value of the random variable at the given time 
	//under the Black Scholes model, i.e. the asset follows a geometric brownian motion
	private static RandomVariable getAssetAtSpecificTime(BrownianMotionInterfaceEnhanced brownian, int indexFactor,
			double riskFreeRate, double sigma, double initialValue, double time) {

		DoubleUnaryOperator geometricBrownian = b -> {
			return initialValue * Math.exp((riskFreeRate - sigma * sigma * 0.5) * time + sigma * b);
		};

		RandomVariable asset = brownian.getBrownianMotionAtSpecificTime(indexFactor, time).apply(geometricBrownian);

		return asset;

	}


}