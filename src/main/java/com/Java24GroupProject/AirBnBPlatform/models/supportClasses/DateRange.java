package com.Java24GroupProject.AirBnBPlatform.models.supportClasses;

import java.time.LocalDate;

public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("start date must be before end date");
        } else if (endDate.isEqual(startDate))
            throw new IllegalArgumentException("start date not be equal to' end date");
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isWithinAnotherDateRange(DateRange availableDateRange) {
        return (this.startDate.isEqual(availableDateRange.startDate) || this.startDate.isAfter(availableDateRange.startDate))
                && (this.endDate.isEqual(availableDateRange.endDate) || this.endDate.isBefore(availableDateRange.endDate));
    }

    public boolean isIdenticalToAnotherDateRange(DateRange dateRange) {
        return (this.startDate.isEqual(dateRange.startDate) && this.endDate.isEqual(dateRange.endDate));
    }

    public boolean hasOverlapWithAnotherDateRange(DateRange dateRange) {
        return !((this.endDate.isEqual(dateRange.startDate) || this.endDate.isBefore(dateRange.startDate))
                || (dateRange.endDate.isEqual(this.startDate) || dateRange.endDate.isBefore(this.startDate)));
    }
}
