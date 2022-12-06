package com.lorenzoberti.session04;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session03.BrownianMotionEnhanced;
import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class implements the ProcessSimulation Interface. It represents the
 * discretization and simulation of a stochastic process under the Black Scholes
 * model, i.e. the process is lognormal distributed: d(X(t)) = mu X(t) dt +
 * sigma X(t) dB(t). Here we use the Euler scheme in order to discretize and
 * simulate the process. Recall that the Euler scheme is: X(t_{i+1}) = mu(t_i,
 * X_{t_i}) (t_{i+1} - t_{i}) + sigma(t_i, X_{t_i}) (W_{t+1} - W_{t_i})
 * 
 * @author Lorenzo Berti
 *
 */
public class EulerSchemeBlackScholes implements ProcessSimulator {

	private BrownianMotionInterfaceEnhanced brownian;
	private double initialValue;
	private double mu;
	private double sigma;
	private TimeDiscretization times;
	private int numberOfPaths;

	private RandomVariable[] allPaths;

	public EulerSchemeBlackScholes(double initialValue, double mu, double sigma, TimeDiscretization times,
			int numberOfPaths) {
		super();
		this.initialValue = initialValue;
		this.mu = mu;
		this.sigma = sigma;
		this.times = times;
		this.numberOfPaths = numberOfPaths;
		this.brownian = new BrownianMotionEnhanced(times, 1, numberOfPaths);
	}
	
	public EulerSchemeBlackScholes(double initialValue, double mu, double sigma, TimeDiscretization times,
			int numberOfPaths, int seed) {
		super();
		this.initialValue = initialValue;
		this.mu = mu;
		this.sigma = sigma;
		this.times = times;
		this.numberOfPaths = numberOfPaths;
		this.brownian = new BrownianMotionEnhanced(times, 1, numberOfPaths, seed);
	}

	public EulerSchemeBlackScholes(BrownianMotionInterfaceEnhanced brownian, double initialValue, double mu, double sigma) {
		super();
		this.brownian = brownian;
		this.initialValue = initialValue;
		this.mu = mu;
		this.sigma = sigma;
		this.times = brownian.getTimeDiscretization();
	}

	@Override
	public double getInitialValue() {
//		if (allPaths == null) {
//			generate();
//		}

		return initialValue;
	}
	
	public int getNumberOfPaths() {
		
		return numberOfPaths;
	}

	@Override
	public BrownianMotionInterfaceEnhanced getStochasticDriver() {
//		if (allPaths == null) {
//			generate();
//		}

		return brownian;
	}

	@Override
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) {
		if (allPaths == null) {
			generate();
		}

		return allPaths[timeIndex];
	}

	@Override
	public RandomVariable getProcessAtGivenTime(double time) {
		if (allPaths == null) {
			generate();
		}

		return allPaths[times.getTimeIndex(time)];
	}

	@Override
	public RandomVariable[] getAllPaths() {
		if (allPaths == null) {
			generate();
		}

		return allPaths;
	}

	@Override
	public double[] getSpecificPath(int indexPath) {
		if (allPaths == null) {
			generate();
		}

		double[] path = new double[times.getNumberOfTimes() + 1];

		path[0] = initialValue;

		for (int i = 0; i < times.getNumberOfTimeSteps(); i++) {

			path[i] = allPaths[i].get(indexPath);

		}

		return path;
	}


	@Override
	public double getSpecificValueOfSpecificPath(int pathIndex, int timeIndex) {
		if (allPaths == null) {
			generate();
		}

		return getSpecificPath(pathIndex)[timeIndex];
	}

	@Override
	public void printPath(int pathIndex) {

		DoubleUnaryOperator trajectory = t -> {
			return getSpecificValueOfSpecificPath(pathIndex, (int) t);
		};

		Plot2D plot = new Plot2D(0, times.getNumberOfTimes(), times.getNumberOfTimes(), trajectory);
		plot.setTitle("Simulated process path");
		plot.setXAxisLabel("Time");
		plot.setYAxisLabel("Process");
		plot.show();

	}

	private void generate() {
		
		allPaths = new RandomVariable[times.getNumberOfTimes()];
		
		allPaths[0] = new RandomVariableFromDoubleArray(initialValue);
		
		for(int timeIndex = 0; timeIndex < times.getNumberOfTimes() - 1; timeIndex++) {
			
			RandomVariable drift = allPaths[timeIndex].mult(mu).mult(times.getTimeStep(timeIndex));
			RandomVariable diffusion = allPaths[timeIndex].mult(sigma).mult(brownian.getBrownianIncrement(0, timeIndex));
			
			allPaths[timeIndex+1] = allPaths[timeIndex].add(drift).add(diffusion);
			
		}


	}

	

	
	
}
