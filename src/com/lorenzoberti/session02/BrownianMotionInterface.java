/**
 * 
 */
package com.lorenzoberti.session02;

/**
 * @author Lorenzo Berti
 *
 */
public interface BrownianMotionInterface {
	
	/**
	 * It returns a two dimensional array of objects of type doubles representing all
	 * the paths of the Brownian motion.
	 * 
	 * @return the entire path of the Brownian motion
	 */
	double[][] getPaths();

	/**
	 * This method returns an array representing the value of the Brownian
	 * motion at a specific time index.
	 * 
	 * @param timeIndex
	 * @return an array which represents the Brownian motion at a given
	 *         time index
	 */
	double[] getProcessAtTimeIndex(int timeIndex);

	/**
	 * This method returns the specified path of the Brownian motion.
	 * 
	 * @param path
	 * @return the specific path of the Brownian motion
	 */
	double[] getSpecificPath(int path);

	/**
	 * This method return the value of a specific path the Brownian motion at a
	 * given time index.
	 * 
	 * @param path
	 * @param timeIndex
	 * @return specific value of the Brownian motion
	 */
	double getSpecificValueAtTimeIndex(int path, int timeIndex);
	
	
	/**
	 * Print the specific path of the Brownian Motion.
	 * 
	 * @param path
	 */
	void printPath(int path);
	
	

	
}