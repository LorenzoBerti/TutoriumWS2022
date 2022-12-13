package com.lorenzoberti.session05;

import java.text.DecimalFormat;
import java.util.function.DoubleUnaryOperator;

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
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to implement the pricing of a call option using the different
 * class that implements the ProcessSimulator interface.
 * 
 * @author Lorenzo Berti
 *
 */
public class CallTest {

	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {

		int numberOfPaths = 10000;
		int seed = 3031;
		
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		double strike = 100.0;
		double maturity = finalTime;
		double forward = initialValue * Math.exp(riskFree * maturity);
		double payoffUnit = Math.exp(-riskFree * maturity);

		AbstractEulerScheme processBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, riskFree,
				sigma);
		AbstractEulerScheme processLogBS = new LogEulerBlackScholes(numberOfPaths, initialValue, times, riskFree,
				sigma);
		AbstractEulerScheme processBachelier = new BachelierEulerScheme(numberOfPaths, initialValue, times, riskFree,
				sigma);

		RandomVariable lastValueBS = processBS.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueLogBS = processLogBS.getProcessAtGivenTime(finalTime);
		RandomVariable lastValueBachelier = processBachelier.getProcessAtGivenTime(finalTime);

		DoubleUnaryOperator payoff = x -> {
			return Math.max(x - strike, 0.0);
		};

		double priceBS = lastValueBS.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double priceLogBS = lastValueLogBS.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);
		double priceBachelier = lastValueBachelier.apply(payoff).getAverage() * Math.exp(-riskFree * maturity);

		double analyticPriceBS = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		double analyticPriceBachelier = AnalyticFormulas.bachelierOptionValue(forward, sigma, maturity, strike,
				payoffUnit);
		
		// Finmath Lib price with Euler scheme
		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFree, sigma);
		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfPaths, seed);
		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);		
		MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);
		EuropeanOption option = new EuropeanOption(maturity, strike);
		double priceFin = option.getValue(blackScholesMonteCarloModel);
		
		System.out.println("\t\t\t  Price\t\tError");
		System.out.println();
		System.out.println(
				"Analytic price BS.......: " + FORMATTERPOSITIVE.format(analyticPriceBS) + " \t"
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - analyticPriceBS) / analyticPriceBS));
		System.out
				.println("Euler BS process........: " + FORMATTERPOSITIVE.format(priceBS) + " \t"
						+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - priceBS) / analyticPriceBS));
		System.out
		.println("Euler LogBS process.....: " + FORMATTERPOSITIVE.format(priceLogBS) + " \t"
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - priceLogBS) / analyticPriceBS));
		
		System.out
		.println("Finmath lib price.......: " + FORMATTERPOSITIVE.format(priceFin) + " \t"
				+ FORMATTERPERCENTAGE.format(Math.abs(analyticPriceBS - priceFin) / analyticPriceBS));
		
		System.out.println();
		
		System.out.println(
				"Analytic price Bachelier: " + FORMATTERPOSITIVE.format(analyticPriceBachelier) + " \t"
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - analyticPriceBachelier) / analyticPriceBachelier));
		System.out.println("Euler Bachelier process.: " + FORMATTERPOSITIVE.format(priceBachelier)
				+ " \t"
				+ FORMATTERPERCENTAGE
						.format(Math.abs(analyticPriceBachelier - priceBachelier) / analyticPriceBachelier));


	}
	
}