/**
 * 
 */
package com.lorenzoberti.session07;

import java.util.Map;

import com.lorenzoberti.session04.ProcessSimulator;

import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
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
	
	private double maturity;
	private double strike;
	private CallOption call;
	private PutOption put;

	public StraddleOption(double maturity, double strike) {
		super();
		this.maturity = maturity;
		this.strike = strike;
		this.call = new CallOption(maturity, strike);
		this.put = new PutOption(maturity, strike);
	}

	@Override
	public RandomVariable getPrice(ProcessSimulator process, RandomVariable discountFactor) {
		
		RandomVariable callPrice = call.getPrice(process, discountFactor);
		RandomVariable putPrice = put.getPrice(process, discountFactor);
		return callPrice.add(putPrice).average();
	}

	@Override
	public double getPriceAsDouble(ProcessSimulator process, RandomVariable discountFactor) {
		
		return getPrice(process, discountFactor).getAverage();
	}
	
	public double getDeltaCentralDifference(ProcessSimulator process, double shift, RandomVariable discountFactor) {
		
		double callDelta = call.getDeltaCentralDifference(process, shift, discountFactor);
		double putDelta = put.getDeltaCentralDifference(process, shift, discountFactor);
		
		return callDelta+putDelta;
		
	}
	
	// This method allows us to print the payoff of the strategy as a function of
	// the underlying value
		public double getPayoffStrategy(double processValue) {

			double callPayoff = Math.max(processValue - strike, 0);
			double putPayoff = Math.max(strike - processValue, 0);
			return (callPayoff + putPayoff);

		}

}