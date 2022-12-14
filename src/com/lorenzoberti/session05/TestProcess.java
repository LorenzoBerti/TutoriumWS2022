package com.lorenzoberti.session05;

import com.lorenzoberti.session03.BrownianMotionEnhanced;
import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;
import com.lorenzoberti.session04.EulerSchemeBlackScholes;
import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to test your process implementation.
 * 
 * @author Lorenzo Berti
 *
 */
public class TestProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		BrownianMotionInterfaceEnhanced brownian = new BrownianMotionEnhanced(times, 1, numberOfPaths);

		double initialValue = 100.0;
		double mu = 0.1;
		double sigma = 0.2;

		// Write your constructors ("old" EulerSchemeBlackScholes, "new" BlackScholesEulerScheme)
		ProcessSimulator process = new EulerSchemeBlackScholes(brownian, initialValue, mu, sigma);
		AbstractEulerScheme processNew = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, mu, sigma);
		AbstractEulerScheme processLog = new LogEulerBlackScholes(numberOfPaths, initialValue, times, mu, sigma);

		// Take the average and the variance and print the values
		RandomVariable lastValueOld = process.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueNew = processNew.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueLog = processLog.getProcessAtGivenTime(finalTime);

		double averageAnalytic = getAverageGeometricBM(initialValue, mu, finalTime);
		double averageOld = lastValueOld.getAverage();
		double averageNew = lastValueNew.getAverage();
		double averageLog = lastValueLog.getAverage();

		System.out.println("The analytic average is...: " + averageAnalytic);
		System.out.println("The old average is........: " + averageOld);
		System.out.println("The new average is........: " + averageNew);
		System.out.println("The log average is........: " + averageLog);

		System.out.println();

		double varianceAnalytic = getVarianceGeometricBM(initialValue, mu, sigma, finalTime);
		double varianceOld = lastValueOld.getVariance();
		double varianceNew = lastValueNew.getVariance();
		double varianceLog = lastValueLog.getVariance();

		System.out.println("The analytic variance is..: " + varianceAnalytic*100);
		System.out.println("The old variance is.......: " + varianceOld);
		System.out.println("The new variance is.......: " + varianceNew);
		System.out.println("The log variance is.......: " + varianceLog);


	}
	
	private static double getAverageGeometricBM(double initialValue, double mu, double time) {
		return initialValue*Math.exp(mu*time);
	}
	
	private static double getVarianceGeometricBM(double initialValue, double mu, double sigma, double time) {
		return initialValue*Math.exp(2*mu*time)*(Math.exp(sigma*sigma*time)-1);
	}
	


}