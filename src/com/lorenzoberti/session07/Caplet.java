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
/**
 * @author Lorenzo Berti
 *
 */
public class Caplet implements FinancialProductInterface {

	private final double strike;
	private final double notional;
	private final double paymentDate;
	private final double fixingDate;
	private final double timeLength;

	public Caplet(double fixingDate, double paymentDate, double strike, double notional) {
		this.strike = strike;
		this.notional = notional;
		this.fixingDate = fixingDate;
		this.paymentDate = paymentDate;
		this.timeLength = paymentDate - fixingDate;
	}
	

	@Override
	public RandomVariable getPrice(ProcessSimulator process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(fixingDate).sub(strike).floor(0.0);
		RandomVariable price = payoff.mult(discountFactor).mult(timeLength).mult(notional);

		return price.average();
	}

	@Override
	public double getPriceAsDouble(ProcessSimulator process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(fixingDate).sub(strike).floor(0.0);
		RandomVariable price = payoff.mult(discountFactor).mult(timeLength).mult(notional);

		return price.getAverage();
	}


	@Override
	public double getDeltaCentralDifference(ProcessSimulator process, double shift, RandomVariable discountFactor) {
		
		double initialValue = process.getInitialValue();
		ProcessSimulator  processUpShift = process.getCloneWithModifiedInitialValue(initialValue+shift);
		ProcessSimulator processDownShift = process.getCloneWithModifiedInitialValue(initialValue-shift);
		double capletUp = getPriceAsDouble(processUpShift, discountFactor);
		double capletDown = getPriceAsDouble(processDownShift, discountFactor);
		
		return (capletUp-capletDown)/(2*shift);
	}
	
}
