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
		
		
		
		
		// Check: there exists an analytic formula for the exchange option that can be
		// recovered through the change of measure...
		
		

		System.out.println("Analytic price is......: ");

	}

}
