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

	private RandomVariable[] allPaths;

	// This two method must be implemented by the specific class because they
	// are specific of the dynamics of the process.
	// Feel free to change the parameters if you need.
	protected abstract RandomVariable getDrift(RandomVariable lastRealization, int timeIndex);

	protected abstract RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex);
	
	protected DoubleUnaryOperator transform; // exp for logEuler
	protected DoubleUnaryOperator inverseTransform;// log for logEuler



	@Override
	public double getInitialValue() {

		return initialValue;
	}

	@Override
	public BrownianMotionInterfaceEnhanced getStochasticDriver() {

		return null;
	}

	public TimeDiscretization getTimeDiscretization() {

		return null;
	}

	@Override
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) {

		return null;
	}

	@Override
	public RandomVariable getProcessAtGivenTime(double time) {
	
		return null;
	}

	@Override
	public RandomVariable[] getAllPaths() {

		return null;
	}

	@Override
	public double[] getSpecificPath(int indexPath) {

		return null;
	}
	
	@Override
	public double getSpecificValueOfSpecificPath(int pathIndex, int timeIndex) {

		return 0;
	} 			

	// This method generate the Euler scheme for a generical process
	private void generate() {

		
	}

	@Override
	public void printPath(int pathIndex) {

	}
	
}