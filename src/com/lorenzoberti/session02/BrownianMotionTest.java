/**
 * 
 */
package com.lorenzoberti.session02;

/**
 * Use this class to test your implementation of the BrownianMotionInterface.
 * 
 * @author Lorenzo Berti
 *
 */
public class BrownianMotionTest {

		private static int numberOfPaths = 100000;
		private static int numberOfTimeSteps = 100;
		private static double timeStep = 1.0;

		/**
		 * @param args
		 */
		public static void main(String[] args) {

			// Create an object of type BrownianMotion
			// replace the following line with the constructor of the Brownian motion
			BrownianMotionInterface brownian = null;			
			
			// Take the process at a given time
			double[] process = null;

			// Check the average (it should be 0)
			System.out.println("The average is: " + getAverage(process));

			// Check the variance (it should be == time)
			System.out.println("The variance is: " + getVariance(process));

			// Check the covariance between B(s) and B(t) (it should be min(s,t))
			double[] process2 = null;			
			System.out.println("The covariance is: " + getCovariance(process, process2));

		}

		// This method return the average of a given array
		private static double getAverage(double[] array) {

			double sum = 0;
			for (int i = 0; i < array.length; i++) {
				sum += array[i];
			}
			double average = (sum / numberOfPaths);
			return average;

		}

		// This method return the variance of a given array
		private static double getVariance(double[] array) {

			double sumSquared = 0;
			for (int i = 0; i < array.length; i++) {
				sumSquared += Math.pow(array[i], 2);
			}

			double variance = sumSquared / numberOfPaths - Math.pow(getAverage(array), 2);

			return variance;
		}

		// This method return the covariance between two given arrays
		private static double getCovariance(double[] array1, double[] array2) {

			double[] product = new double[array1.length];
			for (int i = 0; i < array1.length; i++) {
				product[i] = array1[i] * array2[i];
			}

			double covariance = getAverage(product) - getAverage(array1) * getAverage(array2);

			return covariance;

		}

}
