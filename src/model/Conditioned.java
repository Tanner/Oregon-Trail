package model;

/**
 * 
 * Interface to ensure that something with a condition can have the percentage gotten for a progress bar.
 */
public interface Conditioned {

	/**
	 * Returns the current condition as a percentage of the max.
	 * @return The current condition as a percentage of the max.
	 */
	public abstract double getConditionPercentage();
}
