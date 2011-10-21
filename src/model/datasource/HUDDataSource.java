package model.datasource;

import model.Condition;

/**
 * Data Source for the {@code HUD}
 * 
 * Allows {@code HUD} to get data without being able to modify the data 
 * (which it shouldn't).
 */
public interface HUDDataSource {
	/**
	 * Get the overall {@code Party} health as far as {@code Person} go.
	 * @return Overall condition health for party members
	 */
	Condition getPartyMembersHealth();
	
	/**
	 * Get the status for the {@code Vehicle}.
	 * @return Condition of the vehicle
	 */
	Condition getVehicleStatus();
}
