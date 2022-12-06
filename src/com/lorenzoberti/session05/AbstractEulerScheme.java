package com.lorenzoberti.session05;

import java.util.function.DoubleUnaryOperator;

import com.lorenzoberti.session03.BrownianMotionEnhanced;
import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;
import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.Plot2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This abstract class implements the ProcessSimulator. It simulates a general
 * process dX(t) = mu(t,X(t))dt + sigma(t,X(t))dW(t) by using the EulerScheme,
 * i.e. X(t_{i+1}) =
 * m(t_i,X(t_i))*(t_{i+1}-t_i)+sigma(t_i,X(t_i))*(W(t_{i+1})-W(t_i))
 * 
 * @author Lorenzo Berti
 *
 */
public abstract class AbstractEulerScheme implements ProcessSimulator {

	private BrownianMotionInterfaceEnhanced brownian;
	private double initialValue;
	private TimeDiscretization times;
	int numberOfSimulations;

	private RandomVariable[] allPaths;

	// This two method must be implemented by the specific class because they
	// are specific of the dynamics of the process.
	// Feel free to change the parameters if you need.
	protected abstract RandomVariable getDrift(RandomVariable lastRealization, int timeIndex);

	protected abstract RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex);
	
	protected DoubleUnaryOperator transform; // exp for logEuler
	protected DoubleUnaryOperator inverseTransform;// log for logEuler

	protected AbstractEulerScheme(int numberOfSimulations, double initialValue, TimeDiscretization times) {
		this.initialValue = initialValue;
		this.times = times;
		this.numberOfSimulations = numberOfSimulations;
		this.brownian = new BrownianMotionEnhanced(times, 1, numberOfSimulations);
	}


	@Override
	public double getInitialValue() {

		return initialValue;
	}
	
	public int getNumberOfPaths() {
		
		return numberOfSimulations;
	}

	@Override
	public BrownianMotionInterfaceEnhanced getStochasticDriver() {

		return brownian;
	}

	public TimeDiscretization getTimeDiscretization() {

		return times;
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

	// This method generate the Euler scheme for a generical process
	private void generate() {

		RandomVariable drift;
		RandomVariable diffusion;

		allPaths = new RandomVariable[times.getNumberOfTimes()];

		allPaths[0] = new RandomVariableFromDoubleArray(times.getTime(0), initialValue);

		for (int timeIndex = 1; timeIndex < times.getNumberOfTimes(); timeIndex++) {

			// Apply the inverse transform and do the Euler Scheme...
			RandomVariable inverseOfLastSimulation = allPaths[timeIndex-1].apply(inverseTransform);
			drift = getDrift(inverseOfLastSimulation, timeIndex-1);
			diffusion = getDiffusion(inverseOfLastSimulation, timeIndex-1);
			RandomVariable simulatedInverseTransform = inverseOfLastSimulation.add(drift).add(diffusion);
			//...then transform back
			allPaths[timeIndex] = simulatedInverseTransform.apply(transform);

		}
		
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
	
	
}