package com.ROIIM.Assignment.model;

public class DateOfBirth {

    public Integer day;

    public Integer month;

    public Integer year;

    public DateOfBirth(){}

    public DateOfBirth( int day, int month, int year ){

        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return "DateOfBirth{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
