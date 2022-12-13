/**
 * 
 */
package com.lorenzoberti.session07;

import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;

/**
 * This class represents a Caplet.
 * 
 * @author Lorenzo Berti
 *
 */
public class Caplet implements FinancialProductInterface {

	@Override
	public RandomVariable getPrice(ProcessSimulator process, RandomVariable discountFactor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPriceAsDouble(ProcessSimulator process, RandomVariable discountFactor) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDeltaCentralDifference(ProcessSimulator process, double shift, RandomVariable discountFactor) {
		// TODO Auto-generated method stub
		return 0;
	}

}
