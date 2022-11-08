/**
 * 
 */
package com.lorenzoberti.session02;

import java.text.DecimalFormat;

/**
 * @author Lorenzo Berti
 *
 */
public class MonteCarloExperiment {
	
	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.00000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numberOfDraw = 1000000;
		
		// Example: uniform distribution on [0,1]
		double sumUniform = 0;
		for(int s = 0; s <numberOfDraw; s++) {
			sumUniform+=Math.random(); // draw a number from uniform [0,1]
		}
		double expectationUniform = sumUniform/numberOfDraw;
		
		// Example: uniform distribution on [a,b]
		double a = 4;
		double b = 10;
		double sumUniform_ab = 0;
		for(int t = 0; t < numberOfDraw; t++) {
			double draw = Math.random(); // draw a number from uniform [0,1]
			double draw_ab = (b-a)*draw+a;  // transform it in a draw from uniform on [a,b]
			sumUniform_ab += draw_ab;
		}
		double expectationUniform_ab = sumUniform_ab/numberOfDraw;
		
		System.out.println("Expectation uniform on [0,1]........: " +expectationUniform + "\t Error...: "+Math.abs(expectationUniform-1/2));
		System.out.println("Expectation uniform on ["+a+","+b+"]...: " +expectationUniform_ab + "\t\t Error...: "+Math.abs((a+b)/2 - expectationUniform_ab));
		
		
		
	}

}