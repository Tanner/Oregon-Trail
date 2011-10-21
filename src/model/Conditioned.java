package model;

/**
 * Interface to ensure that something with a condition can 
 * have the percentage gotten for a progress bar.
 * @author Null && Void
 */
public interface Conditioned {

	/**
	 * Returns the current condition as a percentage of the max.
	 * @return The current condition as a percentage of the max.
	 */
	double getConditionPercentage();
}
