package model.datasource;

/**
 * Data Source for the {@code HUD}
 * 
 * Allows {@code HUD} to get data without being able to modify the data 
 * (which it shouldn't).
 */
public interface HUDDataSource {
	/**
	 * Returns the money
	 * @return the party's money
	 */
	public int getMoney();
}
