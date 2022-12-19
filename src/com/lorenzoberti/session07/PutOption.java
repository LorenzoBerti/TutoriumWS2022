package com.lorenzoberti.session07;

import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.stochastic.RandomVariable;

/**
 * @author Lorenzo Berti
 *
 */
public class PutOption implements FinancialProductInterface{

	private final double strike;
	private final double maturity;

	public PutOption(double maturity, double strike) {
		this.maturity = maturity;
		this.strike = strike;
	}

	@Override
	public RandomVariable getPrice(ProcessSimulator process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(maturity).mult(-1).add(strike).floor(0.0);

		return payoff.mult(discountFactor).average();
	}

	@Override
	public double getPriceAsDouble(ProcessSimulator process, RandomVariable discountFactor) {

		RandomVariable payoff = process.getProcessAtGivenTime(maturity).mult(-1).add(strike).floor(0.0);
		RandomVariable price = payoff.mult(discountFactor);

		return price.getAverage();
	}
	
	public double getDeltaCentralDifference(ProcessSimulator process, double shift, RandomVariable discountFactor) {
		
		double initialValue = process.getInitialValue();
		ProcessSimulator  processUpShift = process.getCloneWithModifiedInitialValue(initialValue+shift);
		ProcessSimulator processDownShift = process.getCloneWithModifiedInitialValue(initialValue-shift);
		double putUp = getPriceAsDouble(processUpShift, discountFactor);
		double putDown = getPriceAsDouble(processDownShift, discountFactor);
		
		return (putUp-putDown)/(2*shift);
}


}