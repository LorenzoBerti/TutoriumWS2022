/**
 * 
 */
package com.lorenzoberti.session03;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.DoubleTernaryOperator;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.MultiAssetBlackScholesModel;
import net.finmath.montecarlo.model.AbstractProcessModel;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.montecarlo.process.MonteCarloProcess;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

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
	 * @throws CalculationException 
	 */
	public static void main(String[] args) throws CalculationException {

		int numberOfPaths = 2000000;
		int numberOfFactors = 2;
		int seed = 3031;

		// Time parameter
		double initialTime = 0.0;
		double finalTime = 3.0;
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

		// Since we want to use objects of type RandomVariable we create four no
		// stochastic random variable using the model parameter
		// Note: a costant c can be seen as a trivial random variable X s.t. P(X = c) =
		// 1, P(X != c) = 0
		RandomVariable firstAssetInitialValue = new RandomVariableFromDoubleArray(firstAssetInitial);
		RandomVariable secondAssetInitialValue = new RandomVariableFromDoubleArray(secondAssetInitial);

		RandomVariable firstAssetVol = new RandomVariableFromDoubleArray(firstAssetVolDouble);
		RandomVariable secondAssetVol = new RandomVariableFromDoubleArray(secondAssetVolDouble);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		// Constructor of our Brownian motion
		BrownianMotionInterfaceEnhanced brownian = new BrownianMotionEnhanced(times, numberOfFactors, numberOfPaths, seed);
		
		// DoubleTernaryOperator (from the finmath-lib)
		// x = Brownian motion at maturity, y = sigma, z = InitialValue
		DoubleTernaryOperator geometricBrownian = (x, y, z) -> {
			return z * Math.exp((riskFree - y * y * 0.5) * maturity + y * x);
		};
		
		// First asset at maturity
		RandomVariable firstBrownian = brownian.getBrownianMotionAtSpecificTime(0, maturity);
		RandomVariable firstAsset = firstBrownian.apply(geometricBrownian, firstAssetVol, firstAssetInitialValue); //S^1(T)

		// Second asset at maturity
		RandomVariable secondBrownian = brownian.getBrownianMotionAtSpecificTime(1, maturity);
		
		// Create the correlated Brownian motion
		RandomVariable correlatedBrownian = firstBrownian.mult(correlationFactor)
				.add(secondBrownian.mult(Math.sqrt(1-correlationFactor*correlationFactor)));
		
		double covariance = firstBrownian.covariance(correlatedBrownian).getAverage();
		double variance1 = firstBrownian.getVariance();
		double variance2 = correlatedBrownian.getVariance();
		System.out.println("Correlation factor check...: "+ covariance/Math.sqrt(variance1*variance2));

		RandomVariable secondAsset = correlatedBrownian.apply(geometricBrownian, secondAssetVol, secondAssetInitialValue); // S^2(T)
		
		// Note the methods of the RandomVariable interface!
		double price = firstAsset.sub(secondAsset).floor(0.0).getAverage(); // Monte Carlo price

		// discounting...
		price = price * Math.exp(-riskFree * maturity);
		
		System.out.println();

		System.out.println("Monte Carlo price is...: " + price);
		
		// Check: there exists an analytic formula for the exchange option that can be
		// recovered through the change of measure...
		double vol = firstAssetVolDouble * firstAssetVolDouble
				- 2 * correlationFactor * firstAssetVolDouble * secondAssetVolDouble
				+ secondAssetVolDouble * secondAssetVolDouble;

		vol = Math.sqrt(Math.abs(vol));
		
		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(firstAssetInitial, 0, vol, maturity,
				secondAssetInitial);

		System.out.println("Analytic price is......: " + analyticPrice);
		
		// Price with the Finmath library
		BrownianMotion uncorrelatedFactors = new BrownianMotionFromMersenneRandomNumbers(times, 2, numberOfPaths, seed);
		double[][] factorLoadings = { {1, correlationFactor}, {correlationFactor, 1} };
		double[] initialValues = {firstAssetInitial, secondAssetInitial};
		double[] volatilities = {firstAssetVolDouble, secondAssetVolDouble};
		AbstractProcessModel multiAssetBS = new MultiAssetBlackScholesModel(initialValues, riskFree, volatilities, factorLoadings);
		MonteCarloProcess process = new EulerSchemeFromProcessModel(multiAssetBS, uncorrelatedFactors);
		MonteCarloAssetModel multiAssetBSMonteCarloModel = new MonteCarloAssetModel(process);
		RandomVariable firstAssetFinmath = multiAssetBSMonteCarloModel.getAssetValue(finalTime, 0);
		RandomVariable secondAssetFinmath = multiAssetBSMonteCarloModel.getAssetValue(finalTime, 1);
		double priceFinmath = firstAssetFinmath.sub(secondAssetFinmath).floor(0.0).getAverage();
		priceFinmath = priceFinmath *Math.exp(-riskFree*maturity);
		
		System.out.println("Finmath lib price is...: " + priceFinmath);

}
}