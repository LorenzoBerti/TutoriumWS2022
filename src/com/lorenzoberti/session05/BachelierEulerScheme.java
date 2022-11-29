package com.lorenzoberti.session05;

import com.lorenzoberti.session03.BrownianMotionInterfaceEnhanced;

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
	// Note: in the constructor you need to use (at least) the fields used in the AbstractEulerScheme
	// (otherwise it complains..)


	// Implement the method getDrift and getDiffusion

	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
