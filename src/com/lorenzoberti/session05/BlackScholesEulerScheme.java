package com.lorenzoberti.session05;

import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;
import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
* This class implements the methods getDrift and getDiffusion for the
* Black-Scholes model.
* 
* @author Lorenzo Berti
*
*/
public class BlackScholesEulerScheme extends AbstractEulerScheme {

	// Write the fields and the constructor
	// Note: you need to use (at least) the fields used in the AbstractEulerScheme
	// (otherwise it complains..)

	// Implement the method getDrift and getDiffusion

	double mu;
	double sigma;

	public BlackScholesEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times, double mu,
			double sigma) {
		super( numberOfSimulations, initialValue, times);
		this.mu = mu;
		this.sigma = sigma;
		this.transform = (x -> x);
		this.inverseTransform = (x -> x);
	}

	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		TimeDiscretization times = getTimeDiscretization();
		double timeStep = times.getTimeStep(timeIndex);

		return lastRealization.mult(mu).mult(timeStep);
	}

	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		BrownianMotionInterfaceEnhanced brownian = getStochasticDriver();
		RandomVariable brownianIncrement = brownian.getBrownianIncrement(0, timeIndex);
		return lastRealization.mult(sigma).mult(brownianIncrement);
	}
	
	public double getMu() {
		return mu;
	}
	
	public double getSigma() {
		return sigma;
	}
	
	@Override
	public ProcessSimulator getCloneWithModifiedInitialValue(double newInitialValue) {
		
		TimeDiscretization times = getTimeDiscretization();
		int numberOfPaths = getNumberOfPaths();
		//System.out.println("NumberOfPaths: "+numberOfSimulations);
		double mu = getMu();
		double sigma = getSigma();
		return new BlackScholesEulerScheme(numberOfPaths, newInitialValue, times, mu, sigma);
	}

	
	
	


}