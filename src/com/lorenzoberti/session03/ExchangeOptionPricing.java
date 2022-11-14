/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.functions.AnalyticFormulas;


/**
 * In this class we want to price an exchange option, i.e. and option whose
 * payoff is given by max(S_1(T) - S_2(T), 0), where S_1 and S_2 are two risky
 * assets. We consider a simple Black-Scholes model with two risky assets. Try
 * to use the methods of the RandomVariable interface!
 * 
 * @author Lorenzo Berti
 *
 */
public class ExchangeOptionPricing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfPaths = 100000;
		int numberOfFactors = 2;

		// Time parameter
		double initialTime = 0.0;
		double finalTime = 5.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		double maturity = finalTime;

		// Model parameter
		double firstAssetInitial = 100.0;
		double secondAssetInitial = 100.0;

		double firstAssetVolDouble = 0.3;
		double secondAssetVolDouble = 0.5;

		double riskFree = 0.05;
		
		double correlationFactor = 0.2;

		// Since we want to use objects of type RandomVariable we need to create four no stochastic 
		// random variables using the model parameter
		// Note: a costant c can be seen as a trivial random variable X s.t. P(X = c) = 1, P(X != c) = 0
		
		
		
		
		// Check: there exists an analytic formula for the exchange option that can be
		// recovered through the change of measure...
		double vol = firstAssetVolDouble * firstAssetVolDouble
				- 2 * correlationFactor * firstAssetVolDouble * secondAssetVolDouble
				+ secondAssetVolDouble * secondAssetVolDouble;

		vol = Math.sqrt(Math.abs(vol));
		
		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		System.out.println("Analytic price is......: " + analyticPrice);

	}

}
