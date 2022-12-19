/**
 * 
 */
package com.lorenzoberti.session05;

import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;
import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * @author Lorenzo Berti
 *
 */
public class LogEulerBlackScholes extends AbstractEulerScheme{
	
	double mu;
	double sigma;
	
	public LogEulerBlackScholes(int numberOfSimulations, double initialValue, TimeDiscretization times, double mu,
			double sigma) {
		super(numberOfSimulations, initialValue, times);
		this.mu = mu;
		this.sigma = sigma;
		this.transform = (x -> Math.exp(x));
		this.inverseTransform = (x -> Math.log(x));
	}
	
	/*
	 * It gets and returns the drift of the logarithm of a geometric Brownian
	 * motion, computed with the Euler scheme. That is, it simply returns
	 * (mu-sigma^2/2)*(t_k-t_{k-1})
	 */
	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		TimeDiscretization times = getTimeDiscretization();
		return new RandomVariableFromDoubleArray(times.getTime(timeIndex),
				(mu - 0.5 * sigma * sigma) * (times.getTimeStep(timeIndex)));
	}

	/*
	 * It gets and returns the diffusion of the logarithm of a geometric Brownian
	 * motion computed with the Euler scheme. That is, it simply returns
	 * sigma*(W_{t_k}-W_{t_{k-1}).
	 */
	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {

		BrownianMotionInterfaceEnhanced brownian = getStochasticDriver();
		RandomVariable brownianIncrement = brownian.getBrownianIncrement(0, timeIndex);
		return brownianIncrement.mult(sigma);
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
		double mu = getMu();
		double sigma = getSigma();
		return new  LogEulerBlackScholes(numberOfPaths, newInitialValue, times, mu, sigma);
		
	}


	
	


}