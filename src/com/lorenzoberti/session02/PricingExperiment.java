/**
 * 
 */
package com.lorenzoberti.session02;

import java.text.DecimalFormat;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.montecarlo.process.MonteCarloProcess;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class PricingExperiment {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {
		
		// Model parameters
		double initialValue = 100.0;
		double riskFreeRate = 0.05;
		double sigma = 0.2;
		
		// Monte Carlo simulation parameters
		int numberOfSimulation = 100000; // how many simulation we want to do
		int numberOfTimeSteps = 10;
		double timeStep = 1.0;
		
		// Option parameters
		double strike = 100.0;
		double maturity = numberOfTimeSteps*timeStep;
		
		// Pricing with the Finmath library
		
		double initialTime = 0.0;
		int seed = 3130;
		
		// Create the model
		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFreeRate, sigma);
		
		// Discretize the time
		TimeDiscretization timeDiscretization = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);
		
		// Define the stochastic driver
		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(timeDiscretization, 1, numberOfSimulation, seed);
		
		// Simulate the stochastic process
		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);
		
		MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);
		
		// Create the option
		EuropeanOption option = new EuropeanOption(maturity, strike);
		
		// Get the price
		double value = option.getValue(blackScholesMonteCarloModel);
		
		System.out.println("The option price with the Finmath lib is...: "+ FORMATTERPOSITIVE.format(value));
		
		// For Black-Scholes model we have an analytic formula
		double analyticValue = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, sigma, maturity, strike);
		System.out.println("The analytic value of the option is........: " + FORMATTERPOSITIVE.format(analyticValue));
		
		// Now try to price a call option by yourself using your Brownian Motion implementation and 
		// the exact solution of the Black Scholes model
				
		double evaluationTime = 0;
				
		// Write the constructor of you Brownian motion
		BrownianMotionInterface brownian;
				
		// Take the last value of the Brownian motion
		double[] lastBrownianValue;
				
		// Now create an array storing all the value of S(T) (use the exact solution of the SDE)
		double[] finalValue;
				
		// Get the array containing all the payoff
		double[] payoff;
				
		// Now get the price
		double price = 0;
				
		// discounting...
		price = price * Math.exp(-riskFreeRate * maturity);

		// ... to evaluation time
		price = price * Math.exp(riskFreeRate * evaluationTime);

		System.out.println("The price is...............................: " + FORMATTERPOSITIVE.format(price));

	}

}
