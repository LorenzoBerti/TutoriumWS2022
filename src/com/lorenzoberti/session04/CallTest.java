/**
 * 
 */
package com.lorenzoberti.session04;

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
 * Use this class to price a call option with the objects of type
 * ProcessSimulation that you have created. Change the parameters (in particular
 * numberOfPaths, timeStep and maturity of the option) and try to explain the
 * behaviour of what you see.
 * 
 * @author Lorenzo Berti
 *
 */
public class CallTest {

	/**
	 * @param args
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {

		int numberOfPaths = 100000;
		int seed = 3031;

		// time parameters
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// model parameters
		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		// option parameters
		double strike = 100.0;
		double maturity = finalTime;

		// Price with our Process simulator
		ProcessSimulator processEuler = new EulerSchemeBlackScholes(initialValue, riskFree, sigma, times,
				numberOfPaths, seed);

		RandomVariable lastValueEuler = processEuler.getProcessAtGivenTime(maturity);
		
		DoubleUnaryOperator payoff = x -> {
			return Math.max(x - strike, 0);
		};

		double discountFactor = Math.exp(-riskFree * maturity);

		double priceEuler = lastValueEuler.apply(payoff).mult(discountFactor).getAverage();

		System.out.println("Price with Euler scheme...: " + priceEuler);
		
		// Finmath Lib price with Euler scheme
		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFree, sigma);
		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfPaths, seed);
		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);		
		MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);
		EuropeanOption option = new EuropeanOption(maturity, strike);
		double value = option.getValue(blackScholesMonteCarloModel);
					
		System.out.println("Finmath lib price.........: "+ value);
		
		// Analytic price
		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);
		
		System.out.println("Analytic price............: " + analyticPrice);

	}


}
