/**
 * 
 */
package com.lorenzoberti.session02;

import java.text.DecimalFormat;

import net.finmath.randomnumbers.MersenneTwister;

/**
 * @author Lorenzo Berti
 *
 */
public class MonteCarloExperiments {
	
	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.00000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numberOfDraw = 1000000;
		int numberOfExperiments = 5;
		
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
		
		//System.out.println("Expectation uniform...........: " +expectationUniform);
		System.out.println("Expectation uniform on [a,b]..: " +expectationUniform_ab);
		
		System.out.println("------------------------");
		for(int j = 0; j < numberOfExperiments; j++) {
		
		int seed = 3131*j;
		MersenneTwister mersenne = new MersenneTwister(seed);
			
		double sum = 0.0;
		
		for(int i = 0; i<numberOfDraw; i++) {
			double x = Math.random();  // draw a number from the uniform distribution on [0,1]
			double y = Math.random();  // draw a number from the uniform distribution on [0,1]
			if(x*x + y*y < 1) { // Check if they stay inside the circle of radius 1 
				sum ++;
			}
		}
		
		double sumMersenne = 0.0;
		for(int i = 0; i<numberOfDraw; i++) {
			double x = mersenne.nextDouble();
			double y = mersenne.nextDouble();
			if(x*x + y*y < 1) {
				sumMersenne ++;
			}
		}
		
		double piMonteCarlo = 4* sum/numberOfDraw;
		double piMonteCarloMersenne = 4*sumMersenne/numberOfDraw;
		
		System.out.println("Experiment number "+j);
		System.out.println("Approximated pi............: "+FORMATTERPOSITIVE.format(piMonteCarlo)+"   Error...: "
		+Math.abs(piMonteCarlo-Math.PI));
		System.out.println("Approximated pi Mersenne...: "+FORMATTERPOSITIVE.format(piMonteCarloMersenne)
		+"   Error...: "+Math.abs(piMonteCarloMersenne-Math.PI));
		System.out.println("---------------------------------------------------------------------------");
		}
	}

}
