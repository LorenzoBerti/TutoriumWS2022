/**
 * 
 */
package com.lorenzoberti.session02;

import java.text.DecimalFormat;

/**
 * Use this class to test your implementation of the BrownianMotionInterface.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionTest {
	
	static final DecimalFormat FORMATTERPOSITIVE = new DecimalFormat("0.0000");
	static final DecimalFormat FORMATTERPERCENTAGE = new DecimalFormat("0.000%");

		private static int numberOfPaths = 100000;
		private static int numberOfTimeSteps = 100;
		private static double timeStep = 1.0;

		/**
		 * @param args
		 */
		public static void main(String[] args) {

			// Create an object of type BrownianMotion
			// replace the following line with the constructor of the Brownian motion
			BrownianMotionInterface brownian = new BrownianMotionSimple(numberOfPaths, numberOfTimeSteps, timeStep);			
						
			// Take the process at a given time
			double[] process = brownian.getProcessAtTimeIndex(100);

			// Check the average (it should be 0)
			System.out.println("The average is: " + getAverage(process));

			// Check the variance (it should be == time)
			System.out.println("The variance is: " + getVariance(process));

			// Check the covariance between B(s) and B(t) (it should be min(s,t))
			double[] process2 = brownian.getProcessAtTimeIndex(5);			
			System.out.println("The covariance is: " + getCovariance(process, process2));

			brownian.printPath(5);


		}

		private static double getAverage(double[] array) {

			double sum = 0;
			for (int i = 0; i < array.length; i++) {
				sum += array[i];
			}
			double average = (sum / numberOfPaths);
			return average;

		}

		private static double getVariance(double[] array) {

			double sumSquared = 0;
			for (int i = 0; i < array.length; i++) {
				sumSquared += Math.pow(array[i], 2);
			}

			double variance = sumSquared / numberOfPaths - Math.pow(getAverage(array), 2);

			return variance;
		}

		private static double getCovariance(double[] array1, double[] array2) {

			double[] product = new double[array1.length];
			for (int i = 0; i < array1.length; i++) {
				product[i] = array1[i] * array2[i];
			}

			double covariance = getAverage(product) - getAverage(array1) * getAverage(array2);

			return covariance;

		}

}