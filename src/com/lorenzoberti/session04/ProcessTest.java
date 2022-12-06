/**
 * 
 */
package com.lorenzoberti.session04;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session03.BrownianMotionEnhanced;
import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;

import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to check of your simulation are "quite" good. You can use the
 * private method getAssetAtSpecific time to see if your implementation are good
 * enough. Note: the method getAssetAtSpecific generates directly and only the
 * process at the given time and thus not the paths! Your
 * BlackScholesAnalyticProcess should also generate the whole path...
 * 
 * @author Lorenzo Berti
 *
 */
public class ProcessTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 1000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 1.0;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionInterfaceEnhanced brownian = new BrownianMotionEnhanced(times, 1, numberOfPaths);

		double initialValue = 1.0;
		double mu = 0.00;
		double sigma = 0.2;

		ProcessSimulator processEuler = new EulerSchemeBlackScholes(brownian, initialValue, mu, sigma);

		// Take last value of your simulated processes

		RandomVariable lastValueEuler = processEuler.getProcessAtGivenTime(finalTime);
		RandomVariable lastValuePrivate = getAssetAtSpecificTime(brownian, 0, mu, sigma, initialValue, finalTime);

		// Take the average...

		double averageEuler = lastValueEuler.getAverage();
		double averagePrivate = lastValuePrivate.getAverage();
		// Analytic value
		double analyticAverage = initialValue*Math.exp(mu*finalTime);

		// ...and print
		System.out.println("The Euler scheme average is...............: " + averageEuler);
		System.out.println("The average with the private method is....: " + averagePrivate);
		System.out.println("The analytic mean is......................: " + analyticAverage);

		// Take the variance...

		double varianceEuler = lastValueEuler.getVariance();
		double variancePrivate = lastValuePrivate.getVariance();
		// Analytic value
		double analyticVariance = initialValue*initialValue*Math.exp(2*mu*finalTime)*(Math.exp(sigma*sigma*finalTime)-1);
		
		// ...and print
		System.out.println("The Euler scheme variance is..............: " + varianceEuler);
		System.out.println("The variance with the private method is...: " + variancePrivate);
		System.out.println("The analytic variance is..................: " + analyticVariance);

		int pathIndex = 100;

		DoubleUnaryOperator trajectoryEuler = t -> {
			return processEuler.getSpecificValueOfSpecificPath(pathIndex, (int) t);
		};
		

//		Plot2D plot = new Plot2D(0, times.getNumberOfTimes(), times.getNumberOfTimes(),
//				Arrays.asList(new Named<DoubleUnaryOperator>("Euler", trajectoryEuler)));
//
//		plot.setYAxisNumberFormat(new DecimalFormat("0.0")).setTitle("Path simulation").setXAxisLabel("time")
//				.setYAxisLabel("Process");
//
//		plot.setIsLegendVisible(true);
//
//		plot.show();
//
//		Plot2D plotZoom = new Plot2D(40, 60, 20,
//				Arrays.asList(new Named<DoubleUnaryOperator>("Euler", trajectoryEuler)));
//
//		plotZoom.setYAxisNumberFormat(new DecimalFormat("0.0")).setTitle("Path simulation").setXAxisLabel("time")
//				.setYAxisLabel("Process");
//
//		plotZoom.setIsLegendVisible(true);
//
//		plotZoom.show();



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