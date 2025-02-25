package com.Java24GroupProject.AirBnBPlatform.models.supportClasses;


import java.util.Date;

public class DateRange {
    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("start date must be before end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isWithinAnotherDateRange(DateRange availableDateRange) {
        return ((this.startDate.equals(availableDateRange.startDate) || this.startDate.after(availableDateRange.startDate))
                &&(this.endDate.equals(availableDateRange.endDate) || this.endDate.before(availableDateRange.endDate)));
    }

    public boolean areIdentical (DateRange availableDateRange) {
        return haveTheSameStartDate(availableDateRange) && haveTheSameEndDate(availableDateRange);
    }

    public boolean haveTheSameStartDate(DateRange availableDateRange) {
        return (this.startDate.equals(availableDateRange.startDate));
    }

    public boolean haveTheSameEndDate(DateRange availableDateRange) {
        return (this.endDate.equals(availableDateRange.endDate));
    }

}
