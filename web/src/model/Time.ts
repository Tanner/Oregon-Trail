enum Month {
  JANUARY = 0,
  FEBRUARY = 1,
  MARCH = 2,
  APRIL = 3,
  MAY = 4,
  JUNE = 5,
  JULY = 6,
  AUGUST = 7,
  SEPTEMBER = 8,
  OCTOBER = 9,
  NOVEMBER = 10,
  DECEMBER = 11,
}

const MONTH_DATA: Record<Month, { numberOfDays: number; name: string }> = {
  [Month.JANUARY]: { numberOfDays: 31, name: "Jan." },
  [Month.FEBRUARY]: { numberOfDays: 28, name: "Feb." },
  [Month.MARCH]: { numberOfDays: 31, name: "March" },
  [Month.APRIL]: { numberOfDays: 30, name: "April" },
  [Month.MAY]: { numberOfDays: 31, name: "May" },
  [Month.JUNE]: { numberOfDays: 30, name: "June" },
  [Month.JULY]: { numberOfDays: 30, name: "July" },
  [Month.AUGUST]: { numberOfDays: 31, name: "Aug." },
  [Month.SEPTEMBER]: { numberOfDays: 30, name: "Sept." },
  [Month.OCTOBER]: { numberOfDays: 31, name: "Oct." },
  [Month.NOVEMBER]: { numberOfDays: 30, name: "Nov." },
  [Month.DECEMBER]: { numberOfDays: 31, name: "Dec." },
};

export enum TimeOfDay {
  MORNING = "MORNING",
  AFTERNOON = "AFTERNOON",
  EVENING = "EVENING",
  NIGHT = "NIGHT",
}

export class Time {
  private time: number;
  private day: number;
  private month: Month;
  private year: number;

  constructor(time?: number, day?: number, month?: number, year?: number) {
    if (time !== undefined && day !== undefined && month !== undefined && year !== undefined) {
      this.time = time;
      this.day = day;
      this.month = (month - 1) as Month;
      this.year = year;
    } else {
      this.time = Math.floor(Math.random() * 24);
      this.month = Math.floor(Math.random() * 12) as Month;
      const daysInMonth = MONTH_DATA[this.month].numberOfDays;
      this.day = Math.floor(Math.random() * (daysInMonth - 1)) + 1;
      this.year = Math.floor(Math.random() * 10) + 1860;
    }
  }

  getTimeOfDay(): TimeOfDay {
    if (this.time >= 7 && this.time <= 12) {
      return TimeOfDay.MORNING;
    } else if (this.time >= 13 && this.time <= 18) {
      return TimeOfDay.AFTERNOON;
    } else if (this.time >= 19 || this.time <= 0) {
      return TimeOfDay.EVENING;
    } else {
      return TimeOfDay.NIGHT;
    }
  }

  advanceTime(): void {
    this.time = this.time + 1;

    if (this.time === 23) {
      this.day += 1;
      const isLeapYear = (this.year % 4 === 0 && this.year % 100 !== 0) || this.year % 400 === 0;
      const daysInMonth = MONTH_DATA[this.month].numberOfDays + (this.month === Month.FEBRUARY && isLeapYear ? 1 : 0);

      if (this.day > daysInMonth) {
        this.day = 1;
        this.month = ((this.month + 1) % 12) as Month;
        if (this.month === Month.JANUARY) {
          this.year += 1;
        }
      }
    } else if (this.time === 24) {
      this.time = 0;
    }
  }

  getTime(): number {
    return this.time;
  }

  get24HourTime(): string {
    return `${this.time}:00`;
  }

  get12HourTime(): string {
    const hour = (this.time % 12) + 1;
    const period = this.time < 11 || this.time === 23 ? "am" : "pm";
    return `${hour}:00${period}`;
  }

  getDayMonthYear(): string {
    return `${MONTH_DATA[this.month].name} ${this.day}, ${this.year}`;
  }
}
