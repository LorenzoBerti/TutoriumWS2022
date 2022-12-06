/**
 * 
 */
package com.lorenzoberti.session06;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.montecarlo.process.MonteCarloProcess;
import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomOperator;
import net.finmath.stochastic.RandomVariable;
import net.finmath.stochastic.Scalar;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class FiniteDifferenceExperiment {

	/**
	 * @param args
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {
		
		int numberOfPaths = 10000;
		int seed = 3216;
		
		double initialValue = 100.0;
		double riskFreeRate = 0.05;
		double volatility = 0.30;

		double maturity = 5.0;
		double strike = 125.0;

		double finalTime = maturity;
		double timeStep = 5.0;
		
		TimeDiscretization times = new TimeDiscretizationFromArray(0.0, (int)Math.round(finalTime/timeStep), timeStep);
		
		BrownianMotion brownianMotion = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfPaths, seed);
		
		ProcessModel blackScholesModel = new BlackScholesModel(initialValue, riskFreeRate, volatility);
		
		MonteCarloProcess process = new EulerSchemeFromProcessModel(blackScholesModel, brownianMotion);
		
		MonteCarloAssetModel model = new MonteCarloAssetModel(process);
		
		// Bond payoff: 1
		RandomOperator payoffFunctionOne = underlying -> new Scalar(1.0); 
		
		// Forward payoff: S(T)-K
		RandomOperator payoffFunctionForward = underlying -> underlying.sub(strike); 

		// Call option: max(S(T)-K,0)
		RandomOperator payoffFunctionOption = underlying -> underlying.sub(strike).floor(0.0); 
		
		// Digital option: 1_[S(T)>K]
		RandomOperator payoffFunctionDigital = underlying -> underlying.sub(strike).choose(new Scalar(1.0), new Scalar(0.0));
		
		double valueMonteCarlo = getValueOfEuropeanProduct(model, maturity, payoffFunctionOption);
		
		double valueAnalytic = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFreeRate, volatility, maturity, strike);
		
		System.out.println("\t\t\t\tMonte-Carlo\tAnalytic\tStd. Error");
		printResult("Valuation...:", valueMonteCarlo, valueAnalytic);

		System.out.println("\nBond:");
		double valueDeltaBondAnalytic = 0.0;
		checkDeltaApproximationMethods(model, maturity, payoffFunctionOne, valueDeltaBondAnalytic);

		System.out.println("\nForward:");
		double valueDeltaForwardAnalytic = 1.0;
		checkDeltaApproximationMethods(model, maturity, payoffFunctionForward, valueDeltaForwardAnalytic);

		System.out.println("\nEuropean Option:");
		double valueDeltaAnalytic = AnalyticFormulas.blackScholesOptionDelta(initialValue, riskFreeRate, volatility, 
				maturity, strike);
		checkDeltaApproximationMethods(model, maturity, payoffFunctionOption, valueDeltaAnalytic);

		System.out.println("\nDigital Option:");
		double valueDeltaDigitalAnalytic = AnalyticFormulas.blackScholesDigitalOptionDelta(initialValue, riskFreeRate, 
				volatility, maturity, strike);
		checkDeltaApproximationMethods(model, maturity, payoffFunctionDigital, valueDeltaDigitalAnalytic);


		Plot2D plotForward = getPlotDeltaApproximationByShift(-13, -1, model, maturity, payoffFunctionForward, 
				valueDeltaForwardAnalytic);
		plotForward.setTitle("Finite Difference Approximation of Delta of a Forward (f(S(T) = S(T)-K)").show();

		Plot2D plotEuropean = getPlotDeltaApproximationByShift(-13, -1, model, maturity, payoffFunctionOption, 
				valueDeltaAnalytic);
		plotEuropean.setTitle("Finite Difference Approximation of Delta of a European Option (f(S(T) = max(S(T)-K,0))").show();

		Plot2D plotDigital = getPlotDeltaApproximationByShift(-13, -1, model, maturity, payoffFunctionDigital, 
				valueDeltaDigitalAnalytic);
		plotDigital.setTitle("Finite Difference Approximation of Delta of a Digital Option (f(S(T) = 1 for S(T) > K, otherwise 0)").show();

		
	}
	
	private static double getValueOfEuropeanProduct(MonteCarloAssetModel model, double maturity, RandomOperator payoffFunction) throws CalculationException {

		RandomVariable underlying = model.getAssetValue(maturity, 0); // S(T)

		RandomVariable payoff = payoffFunction.apply(underlying); // f(S(T))

		RandomVariable value = payoff.div(model.getNumeraire(maturity).mult(model.getNumeraire(0.0))); // f(S(T)) / N(T) * N(t)

		double price = value.getAverage(); // Monte-Carlo price
		
		return price;
	}
	
	private static double getDeltaCentralFiniteDifference(MonteCarloAssetModel model, double maturity, RandomOperator payoffFunction, 
			double shift) throws CalculationException {
	
			return 0;
		
	}
	
	private static double getDeltaForwardFiniteDifference(MonteCarloAssetModel model, double maturity, 
			RandomOperator payoffFunction, double shift) throws CalculationException {
		
			return 0;
	
}
	
	private static double getDeltaBackwardFiniteDifference(MonteCarloAssetModel model, double maturity, 
			RandomOperator payoffFunction, double shift) throws CalculationException {
		
			return 0;
	
}
	
	private static void checkDeltaApproximationMethods(MonteCarloAssetModel model, double maturity,
			RandomOperator payoffFunction, double valueDeltaAnalytic) throws CalculationException {

		double valueDeltaFiniteDifference1 = getDeltaCentralFiniteDifference(model, maturity, payoffFunction, 1E-1);
		printResult("Central Finite Diff (h=1e-1):", valueDeltaFiniteDifference1, valueDeltaAnalytic);

		double valueDeltaFiniteDifference3 = getDeltaCentralFiniteDifference(model, maturity, payoffFunction, 1E-3);
		printResult("Central Finite Diff (h=1e-3):", valueDeltaFiniteDifference3, valueDeltaAnalytic);
		
		double valueDeltaForwardFiniteDifference1 = getDeltaForwardFiniteDifference(model, maturity, payoffFunction, 1E-1);
		printResult("Forward Finite Diff (h=1e-1):", valueDeltaForwardFiniteDifference1, valueDeltaAnalytic);
		
		double valueDeltaForwardFiniteDifference3 = getDeltaForwardFiniteDifference(model, maturity, payoffFunction, 1E-3);
		printResult("Forward Finite Diff (h=1e-3):", valueDeltaForwardFiniteDifference3, valueDeltaAnalytic);
		
		double valueDeltaBackwardFiniteDifference1 = getDeltaBackwardFiniteDifference(model, maturity, payoffFunction, 1E-1);
		printResult("Backward Finite Diff (h=1e-1):", valueDeltaBackwardFiniteDifference1, valueDeltaAnalytic);
		
		double valueDeltaBackwardFiniteDifference3 = getDeltaBackwardFiniteDifference(model, maturity, payoffFunction, 1E-3);
		printResult("Backward Finite Diff (h=1e-3):", valueDeltaBackwardFiniteDifference3, valueDeltaAnalytic);

	}
	
	private static Plot2D getPlotDeltaApproximationByShift(double xmin, double xmax, MonteCarloAssetModel model, 
			double maturity, RandomOperator payoffFunction, double valueAnalytic) throws CalculationException {
		double initialValue = model.getAssetValue(0.0, 0).doubleValue();

		DoubleUnaryOperator finiteDifferenceApproximationByShift = scale -> {
						try {
							return getDeltaCentralFiniteDifference(
							model, maturity, payoffFunction, initialValue*Math.pow(10, scale));
						} catch (CalculationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return scale;
			};
		
		Plot2D plot = new Plot2D(xmin, xmax, 400,
				Arrays.asList(new Named<DoubleUnaryOperator>("Central Finite Difference", finiteDifferenceApproximationByShift),
						new Named<DoubleUnaryOperator>("Analytic", x -> valueAnalytic)));
		plot.setXAxisLabel("scale (shift = S\u2080*10^{scale})")
		.setYAxisLabel("value")
		.setYAxisNumberFormat(new DecimalFormat("0.#####"));

		return plot;
	}	

	
	private static void printResult(String name, double valueMonteCarlo, double valueAnalytic) {
		System.out.format("%24s\t%+10.6f\t%+10.6f\t%+9.5e\n", name, valueMonteCarlo, valueAnalytic, valueMonteCarlo-valueAnalytic);
	}
	

}
