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

		int numberOfPaths = 100000;
		int seed = 3031;
		
		double initialTime = 0.0;
		double finalTime = 1.0;
		double timeStep = 0.01;
		int numberOfTimeSteps = (int) (finalTime / timeStep);

		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		// Model parameter 
		double initialValue = 100.0;
		double riskFree = 0.1;
		double sigma = 0.2;

		// Option parameter 
		double strike = 100.0;
		double maturity = finalTime;
		

		// Write the constructors for the 3 process (BS Euler, BS LogEuler, Bachelier)...

		
		// Take the processes at final time...

		
		// Take the prices

		
		// Take the Finmath Lib price with Euler scheme
		
		
		// Analytic prices from the FinmathLib
		
		
		// Print and check....
	
	}
	
}
