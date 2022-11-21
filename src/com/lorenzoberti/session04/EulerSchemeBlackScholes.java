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

	@Override
	public double getInitialValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BrownianMotionInterfaceEnhanced getStochasticDriver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomVariable getProcessAtGivenTime(double time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomVariable[] getAllPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getSpecificPath(int pathIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getSpecificValueOfSpecificPath(int pathIndex, int timeIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printPath(int pathIndex) {
		// TODO Auto-generated method stub
		
	}

	
	
}
