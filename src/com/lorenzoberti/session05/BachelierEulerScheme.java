package com.lorenzoberti.session05;

import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;
import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class implements the methods getDrift and getDiffusion for the Bachelier
 * model. Feel free to choose between: Inhomogenous Bachelier Model: dS(t) = r
 * S(t) dt + sigma dW(t) Homogeneous Bachelier Model: dS(t) = r S(t) dt +
 * e^(-r(T-t)) sigma dW(t).
 * 
 * @author Lorenzo Berti
 *
 */
public class BachelierEulerScheme extends AbstractEulerScheme {

	// Write the fields and the constructor
	// Note: you need to use (at least) the fields used in the AbstractEulerScheme
	// (otherwise it complains..)


	// Implement the method getDrift and getDiffusion

	private double mu;
	private double sigma;

	public BachelierEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times, double mu,
			double sigma) {
		super(numberOfSimulations, initialValue, times);
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
		TimeDiscretization times = getTimeDiscretization();
		double currentTime = times.getTime(timeIndex);
		double lastTime = times.getTime(times.getNumberOfTimes() - 1);
		BrownianMotionInterfaceEnhanced brownian = getStochasticDriver();
		RandomVariable brownianIncrement = brownian.getBrownianIncrement(0, timeIndex);
		return brownianIncrement.mult(sigma * Math.exp(-mu * (lastTime - currentTime)));
	}
	

	

	

}