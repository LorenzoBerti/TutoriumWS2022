/**
 * 
 */
package com.lorenzoberti.session03;

import java.util.Arrays;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.DoubleTernaryOperator;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * @author Lorenzo Berti
 *
 */
public class ConfidenceIntervalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double level = 0.025;
		
		int numberOfFactors = 1;
		int numberOfPaths = 1000000;

		// Time parameter
		double initialTime = 0.0;
		double finalTime = 5.0;
		double deltaT = 1.0;
		int numberOfTimeSteps = (int) (finalTime / deltaT);

		// Option parameter
		double maturity = finalTime;
		double strike = 100.0;

		// Model parameter
		double initialValue = 100.0;
		double sigma = 0.3;
		double riskFree = 0.05;

		// Since we want to use objects of type RandomVariable we create four no
		// stochastic random variable using the model parameter
		// Note: a costant c can be seen as a trivial random variable X s.t. P(X = c) =
		// 1, P(X != c) = 0
		RandomVariable assetInitialValue = new RandomVariableFromDoubleArray(initialValue);
		
		RandomVariable assetVol = new RandomVariableFromDoubleArray(sigma);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, deltaT);

		// Constructor of our Brownian motion
		BrownianMotionInterfaceEnhanced brownianMotion = new BrownianMotionEnhanced(times, numberOfFactors, numberOfPaths);

		// DoubleTernaryOperator (from the finmath-lib)
		// x = Brownian motion, y = sigma, z = InitialValue
		DoubleTernaryOperator geometricBrownian = (x, y, z) -> {
			return z * Math.exp((riskFree - y * y * 0.5) * maturity + y * x);
		};
		
		// Asset at maturity
		RandomVariable brownian = brownianMotion.getBrownianMotionAtSpecificTime(0, maturity);
		RandomVariable firstAsset = brownian.apply(geometricBrownian, assetVol, assetInitialValue);


		// Note the methods of the RandomVariable interface!
		RandomVariable price = firstAsset.sub(strike).floor(0.0);

		// discounting...
		price = price.mult(Math.exp(-riskFree * maturity));
		
		ConfidenceInterval confidenceIntervalNormal = new NormalInterval(price);
		double[] confidenceNormal = confidenceIntervalNormal.getConfidenceInterval(numberOfPaths, level);
		System.out.println("The normal confidence interval at level "+(1-level*2)+" is.....: "
				+Arrays.toString(confidenceNormal)+" Lenght:"+(confidenceNormal[1]-confidenceNormal[0]));
		
		ConfidenceInterval confidenceIntervalChebyshev = new ChebyshevInterval(price);
		double[] confidenceChebyshev = confidenceIntervalChebyshev.getConfidenceInterval(numberOfPaths, level);
		System.out.println("The Chebyshev confidence interval at level "+(1-level*2)+" is..: "
				+Arrays.toString(confidenceChebyshev)+" Lenght:"+(confidenceChebyshev[1]-confidenceChebyshev[0]));

		double analyticPrice = AnalyticFormulas.blackScholesOptionValue(initialValue, riskFree, sigma, maturity,
				strike);

		System.out.println("Monte Carlo price is...: " + price.getAverage());
		System.out.println("Analytic price is......: " + analyticPrice);
		
		System.out.println("_".repeat(80) + "\n");

	}

	

}