package model;

public class Time {

	int time;
	
	private enum TimeOfDay {
		MORNING,
		AFTERNOON,
		EVENING,
		NIGHT;
	}
	
	public Time(int time) {
		this.time = time;
	}
	
	public TimeOfDay getTimeOfDay() {
		if( 7 <= time && time <= 12) {
			return TimeOfDay.MORNING;
		} else if ( 13 <= time && time <= 18) {
			return TimeOfDay.AFTERNOON;
		} else if (19 <= time || time <= 0) {
			return TimeOfDay.EVENING;
		} else {
			return TimeOfDay.NIGHT;
		}
	}
	
	public void advanceTime() {
		time = (time + 1)%24;
	}
	
	public int getTime() {
		return time;
	}
}
