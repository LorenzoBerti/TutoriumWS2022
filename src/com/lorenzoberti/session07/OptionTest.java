/**
 * 
 */
package com.lorenzoberti.session07;

import java.text.DecimalFormat;

import com.lorenzoberti.session04.ProcessSimulator;
import com.lorenzoberti.session05.AbstractEulerScheme;
import com.lorenzoberti.session05.BachelierEulerScheme;
import com.lorenzoberti.session05.BlackScholesEulerScheme;
import com.lorenzoberti.session05.LogEulerBlackScholes;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to check your Caplet and Straddle Option
 * class. You can use different model for the underlying.
 * Recall that also for the Caplet exists an analytic value. 
 * 
 * @author Lorenzo Berti
 *
 */
public class OptionTest {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numberOfPaths = 100000;

		// Time parameters
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// Model parameters
		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		// Option parameter
		double strike = 100.0;
		double maturity = finalTime;

		double forward = initialValue * Math.exp(riskFree * maturity);
		double payoffUnit = Math.exp(-riskFree * maturity);
		
		RandomVariable discountFactor = new RandomVariableFromDoubleArray(Math.exp(-riskFree*maturity));
		
		// Call option test

		FinancialProductInterface call = new CallOption(maturity, strike);

		AbstractEulerScheme processEulerBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, riskFree, sigma);
		AbstractEulerScheme logEulerBS = new LogEulerBlackScholes(numberOfPaths, initialValue, times, riskFree, sigma);
		AbstractEulerScheme processBachelier = new BachelierEulerScheme(numberOfPaths, initialValue, times, riskFree, sigma);
		
		double callBSEuler = call.getPriceAsDouble(processEulerBS, discountFactor);
		double callBSLogEuler = call.getPriceAsDouble(logEulerBS, discountFactor);
		double callBachelier = call.getPriceAsDouble(processBachelier, discountFactor);
		
		double analyticPriceBS = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		double analyticPriceBachelier = AnalyticFormulas.bachelierOptionValue(forward, sigma, maturity, strike,
				payoffUnit);
		
		System.out.println("Call Test:");
		System.out.println("                               Price         Error");
		System.out.println();
		System.out.println("Analytic price BS           : " + FORMATTERPOSITIVE.format(analyticPriceBS) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - analyticPriceBS) / analyticPriceBS));
		System.out.println("Euler BS process            : " + FORMATTERPOSITIVE.format(callBSEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - callBSEuler) / analyticPriceBS));
		System.out.println("Log Euler BS process        : " + FORMATTERPOSITIVE.format(callBSLogEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - callBSLogEuler) / analyticPriceBS));
		// System.out.println();
		System.out.println("Analytic price Bachelier    : " + FORMATTERPOSITIVE.format(analyticPriceBachelier)
				+ "         " + FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - analyticPriceBachelier) / analyticPriceBachelier));
		System.out.println("Simulated Bachelier process : " + FORMATTERPOSITIVE.format(callBachelier) + "         "
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - callBachelier) / analyticPriceBachelier));
		
		System.out.println();
		
		// Caplet test
		
		double paymentDate = 2.0;
		double fixingDate = 1.0;

		double initialLiborRate = 0.05;
		double liborVol = 0.3;

		double discountFactorAtMaturity = 0.91;
		RandomVariable discountFactorCaplet = new RandomVariableFromDoubleArray(discountFactorAtMaturity);

		AbstractEulerScheme liborEuler = new BlackScholesEulerScheme(numberOfPaths, initialLiborRate, times, 0, liborVol);
		AbstractEulerScheme logLiborEuler = new LogEulerBlackScholes(numberOfPaths, initialLiborRate, times, 0, liborVol);
		
		double strikeCaplet = 0.044;
		double notional = 1000.0;

		double timeLength = paymentDate - fixingDate;

		Caplet caplet = new Caplet(fixingDate, paymentDate, strikeCaplet, notional);

		double priceCapletEuler = caplet.getPriceAsDouble(liborEuler, discountFactorCaplet);
		double priceCapletLogEuler = caplet.getPriceAsDouble(logLiborEuler, discountFactorCaplet);

		double analyticPriceCaplet = notional * discountFactorAtMaturity * timeLength
				* AnalyticFormulas.blackScholesOptionValue(initialLiborRate, 0, liborVol, timeLength, strikeCaplet);
		
		System.out.println("Caplet Test:");
		System.out.println("                               Price         Error");
		System.out.println();
		System.out.println("Analytic price BS           : " + FORMATTERPOSITIVE.format(analyticPriceCaplet) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceCaplet - analyticPriceCaplet) / analyticPriceCaplet));
		System.out.println("Euler BS process            : " + FORMATTERPOSITIVE.format(priceCapletEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceCaplet - priceCapletEuler) / analyticPriceCaplet));
		System.out.println("Analytic BS process         : " + FORMATTERPOSITIVE.format(priceCapletLogEuler) + "        "
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceCaplet - priceCapletLogEuler) / analyticPriceCaplet));
		


		
	}

}

