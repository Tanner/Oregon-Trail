package model;

import java.util.Random;

public class Time {

	int time, day, year;
	Month month;
	
	private enum TimeOfDay {
		MORNING,
		AFTERNOON,
		EVENING,
		NIGHT;
	}
	
	private enum Month {
		JANUARY(31, "Jan."),
		FEBRUARY(28, "Feb."),
		MARCH(31, "March"),
		APRIL(30, "April"),
		MAY(31, "May"),
		JUNE(30, "June"),
		JULY(30, "July"),
		AUGUST(31, "Aug."),
		SEPTEMBER(30, "Sept."),
		OCTOBER(31, "Oct."),
		NOVEMBER(30, "Nov."),
		DECEMBER(31, "Dec.");
		
		private int numberOfDays;
		private String name;
		
		private Month(int numberOfDays, String name) {
			this.numberOfDays = numberOfDays;
			this.name = name;
		}
		
		public int getNumberOfDays() {
			return numberOfDays;
		}
		
		public String getName() {
			return name;
		}
		public Month getNextMonth() {
			return values()[(ordinal() + 1) % (values().length)];
		}
	}
	
	
	public Time() {
		Random random = new Random();
		this.time = random.nextInt(24);
		this.month = Month.values()[random.nextInt(Month.values().length)];
		this.day = random.nextInt(month.getNumberOfDays() - 1) + 1;
		this.year = random.nextInt(10) + 1860;
	}
	public Time(int time, int day, int month, int year) {
		this.time = time;
		this.day = day;
		this.month = Month.values()[month - 1];
		this.year = year;
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
		time = (time + 1);
		if ( time == 23) {
			day += 1;
			if (day > month.getNumberOfDays() + (month.equals(Month.FEBRUARY) && 
				((year%4 == 0 && year%100 != 0) || (year%400) == 0) ? 1 : 0)) {
				day = 1;
				month = month.getNextMonth();
				if (month == Month.JANUARY) {
					year += 1;
				}
			}
		} else if (time == 24) {
			time = 0;
		}
	}

	public int getTime() {
		return time;
	}
	
	public String get24HourTime() {
		return time + ":00";
	}
	
	public String get12HourTime() {
		return ((time)%12 + 1) + ":00" + ((time < 11 || time == 23)? "am" : "pm");
	}
	
	public String getDayMonthYear() {
		return month.getName() + " " + day + ", " + year;
	}
}
