/**
 * 
 */
package com.lorenzoberti.session07;


import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.NormalDistribution;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromRandomNumberGenerator;
import net.finmath.randomnumbers.MersenneTwister;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class compares the efficiency of some methods we have seen for
 * evaluating a call option under the Black-Scholes Model.
 * 
 * @author Lorenzo Berti
 *
 */
public class MonteCarloOptionValueExperiment {

	// Model parameters
	private double initialValue = 100.0;
	private double riskFreeRate = 0.05;
	private double volatility = 0.2;

	// Option parameters
	private double strike = 100.0;
	private double maturity = 1.0;

	private int seed = 3083;
	private int numberOfSimulation = 10000000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		long timeStart, timeEnd;

		MonteCarloOptionValueExperiment experiment = new MonteCarloOptionValueExperiment();

		timeStart = System.currentTimeMillis();
		double valueAnalytic = experiment.getAnalyticValue();
		timeEnd = System.currentTimeMillis();

		System.out.println(
				"Analytic value...............: " + valueAnalytic + "\t" + (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueForLoop = experiment.getValueUsingForLoop();
		timeEnd = System.currentTimeMillis();

		System.out.println(
				"Value with for loop..........: " + valueForLoop + "\t" + (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueRandomVariable = experiment.getValueUsingRandomVariable();
		timeEnd = System.currentTimeMillis();

		System.out
				.println("Value with RV................: " + valueRandomVariable + "\t"
				+ (timeEnd - timeStart) / 1000.0 + "sec.");

		timeStart = System.currentTimeMillis();
		double valueStreams = experiment.getValueUsingStreams();
		timeEnd = System.currentTimeMillis();

		System.out
				.println("Value with Streams...........: " + valueStreams + "\t" + (timeEnd - timeStart) / 1000.0
						+ "sec.");

	}

	/**
	 * This method evaluates the call option using the object Stream.
	 * 
	 * @return the value of a call option.
	 */
	private double getValueUsingStreams() {


		return 0;
	}

	/**
	 * This method evaluates a call option using the RandomVariable object.
	 * 
	 * @return the value of a call option.
	 */
	private double getValueUsingRandomVariable() {



		return 0;
	}

	/**
	 * This method evaluates a call option using for loop.
	 * 
	 * @return the value of a call option.
	 */
	private double getValueUsingForLoop() {

		return 0;
	}

	/**
	 * This method gives you the analytic value of a call option using the
	 * Black-Scholes formula.
	 * 
	 * @return analytic value of a call option.
	 */
	private double getAnalyticValue() {

		return 0;
	}

}