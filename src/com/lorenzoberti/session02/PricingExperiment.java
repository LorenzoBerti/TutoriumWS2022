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
 * We want to price an European Call Option under the Black-Scholes Model.
 * Black-Scholes Model:
 * dS(t) = \mu S(t) dt + \sigma S(t) dW(t) S(0) = S_0
 * dr(t) = r dt                            r(0) = r
 * 
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
		//price = price * Math.exp(-riskFreeRate * maturity);

		// ... to evaluation time
		//price = price * Math.exp(riskFreeRate * evaluationTime);

		//System.out.println("The price is...............................: " + FORMATTERPOSITIVE.format(price));

	}

}
