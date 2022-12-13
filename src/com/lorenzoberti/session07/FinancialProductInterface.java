package com.lorenzoberti.session07;

import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;

/**
 * This interface represents a generic financial product
 * 
 * @author Lorenzo Berti
 *
 */
public interface FinancialProductInterface {

	/**
	 * This method returns the price as object of type RandomVariable of the
	 * financial product.
	 * 
	 * @param process
	 * @param discountFactor
	 * @return
	 */
	RandomVariable getPrice(ProcessSimulator process, RandomVariable discountFactor);


	/**
	 * This method returns the price as a double of the financial product.
	 * 
	 * @param process
	 * @param discountFactor
	 * @return
	 */
	double getPriceAsDouble(ProcessSimulator process, RandomVariable discountFactor);
	
	/**
	 * This method returns the delta of the financial product, i.e. the first order derivative
	 * respect to the initial value of the underlying.
	 * 
	 * @param process
	 * @param shift
	 * @param discountFactor
	 * @return
	 */
	public double getDeltaCentralDifference(ProcessSimulator process, double shift, RandomVariable discountFactor);


}
