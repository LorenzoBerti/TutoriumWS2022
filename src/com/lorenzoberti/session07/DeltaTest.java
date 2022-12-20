/**
 * 
 */
package com.lorenzoberti.session07;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session05.AbstractEulerScheme;
import com.lorenzoberti.session05.BlackScholesEulerScheme;
import com.lorenzoberti.session05.LogEulerBlackScholes;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.montecarlo.process.MonteCarloProcess;
import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Use this class to implement the hedging strategy of the Straddle Option.
 * 
 * @author Lorenzo Berti
 *
 */
public class DeltaTest {
	
	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");


	/**
	 * @param args
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {
		
		int numberOfPaths = 100000;
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.1;
		int numberOfTimeSteps = (int) (finalTime / timeStep);
		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.3;

		double strike = 100.0;
		double maturity = finalTime;

		RandomVariable discountFactor = new RandomVariableFromDoubleArray(Math.exp(-riskFree * maturity));

		// Analytic price

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity, strike)
				+ AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity, strike, false);

		System.out.println("Straddle Option Experiment\n");
		System.out.println("Analytic price BS.......: " + FORMATTERPOSITIVE.format(analyticPrice));

			// Price with finmath library
			int seed = 3013;

			ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFree, sigma);

			BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfPaths, seed);

			MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);

			MonteCarloAssetModel blackScholesMonteCarloModel = new MonteCarloAssetModel(process);

			EuropeanOption option = new EuropeanOption(maturity, strike);

			// Recover the straddle price from the put-call parity:
			// C_t - P_t = S_t - K * e^{-r(T-t)}
			double valueCallFinmath = option.getValue(blackScholesMonteCarloModel);

			double valueFinmath = 2 * valueCallFinmath - blackScholesMonteCarloModel.getAssetValue(0, 0).getAverage()
					+ strike * Math.exp(-riskFree * maturity);

			// Price with our classes
			AbstractEulerScheme processEulerBS = new BlackScholesEulerScheme(numberOfPaths, initialValue, times, riskFree, sigma);
			AbstractEulerScheme logEulerBS = new LogEulerBlackScholes(numberOfPaths, initialValue, times, riskFree, sigma);

			StraddleOption straddle = new StraddleOption(maturity, strike);

			double eulerPrice = straddle.getPriceAsDouble(processEulerBS, discountFactor);
			double logEulerPrice = straddle.getPriceAsDouble(logEulerBS, discountFactor);

			System.out.println("Price Euler scheme......: " + FORMATTERPOSITIVE.format(eulerPrice) + "\tError: "
					+ FORMATTERPOSITIVE.format(Math.abs(eulerPrice - analyticPrice)));
			;
			System.out.println("Price LogEuler scheme...: " + FORMATTERPOSITIVE.format(logEulerPrice) + "\tError: "
					+ FORMATTERPOSITIVE.format(Math.abs(logEulerPrice - analyticPrice)));
			;
			System.out.println("Price finmath library...: " + FORMATTERPOSITIVE.format(valueFinmath) + "\tError: "
					+ FORMATTERPOSITIVE.format(Math.abs(valueFinmath - analyticPrice)));
			
			System.out.println();

		
			
		DoubleUnaryOperator straddlePayoff = value -> {
			return straddle.getPayoffStrategy(value);
		};

		Plot2D plotStraddle = new Plot2D(0, 200, 200, straddlePayoff);
		plotStraddle.setTitle("Straddle strategy payoff");
		plotStraddle.setXAxisLabel("Stock price");
		plotStraddle.setYAxisLabel("Straddle payoff");
		plotStraddle.setYAxisNumberFormat(FORMATTERPOSITIVE);
		plotStraddle.show();

		// Straddle hedging strategy

		double shift = Math.pow(10, -10);
		
		double deltaEuler = straddle.getDeltaCentralDifference(processEulerBS, shift, discountFactor);
		double deltaLogEuler = straddle.getDeltaCentralDifference(logEulerBS, shift, discountFactor);

		// Analytic delta 
		double deltaCall = AnalyticFormulas.blackScholesOptionDelta(initialValue, riskFree, sigma, maturity, strike);
		double deltaPut = deltaCall - 1;
		double analyticHedging = deltaCall + deltaPut;

		System.out.println("Delta hedging:\n");
		System.out.println("Analytic.......................: " + FORMATTERPOSITIVE.format(analyticHedging) + "\tError: "
				+ FORMATTERPOSITIVE.format(Math.abs(analyticHedging - analyticHedging)));
		System.out.println("Delta Euler Central Diff.......: " + FORMATTERPOSITIVE.format(deltaEuler) + "\tError: "
				+ FORMATTERPOSITIVE.format(Math.abs(deltaEuler - analyticHedging)));
		System.out.println("Delta Log Euler Central Diff...: " + FORMATTERPOSITIVE.format(deltaLogEuler) + "\tError: "
				+ FORMATTERPOSITIVE.format(Math.abs(analyticHedging - deltaLogEuler)));

		DoubleUnaryOperator deltaHedgingLogEuler = scale -> {
			double shiftScaled = Math.pow(10, -scale);
			return straddle.getDeltaCentralDifference(logEulerBS, shiftScaled, discountFactor);
		};
		
		DoubleUnaryOperator deltaHedgingEuler = scale -> {
			double shiftScaled = Math.pow(10, -scale);
			return straddle.getDeltaCentralDifference(processEulerBS, shiftScaled, discountFactor);
		};

		DoubleUnaryOperator analytic = (x) -> analyticHedging;

		Plot2D plotHedging = new Plot2D(10, 15, 10,
				Arrays.asList(new Named<DoubleUnaryOperator>("Central difference LogEuler", deltaHedgingLogEuler),
						new Named<DoubleUnaryOperator>("Central difference Euler", deltaHedgingEuler),
						new Named<DoubleUnaryOperator>("Analytic delta", analytic)));
		plotHedging.setTitle("Delta Hedging strategy");
		plotHedging.setXAxisLabel("Scale: shift = 10^(-scale)");
		plotHedging.setYAxisLabel("Central difference");
		plotHedging.setYAxisNumberFormat(FORMATTERPOSITIVE);
		plotHedging.setIsLegendVisible(true);
		plotHedging.show();

	}

}