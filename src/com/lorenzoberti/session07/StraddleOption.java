/**
 * 
 */
package com.lorenzoberti.session07;

import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;

/**
 * This class represent a Straddle option strategy, i.e. a strategy involving
 * the purchase of both a put and call option on the same underlying security
 * for the same expiration date and strike price.
 * 
 * @author Lorenzo Berti
 *
 */
public class StraddleOption implements FinancialProductInterface{

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
		
		// Hint: you can modify the ProcessSimulator interface and add an additional method...
		
		return 0;
	}
	
	

}