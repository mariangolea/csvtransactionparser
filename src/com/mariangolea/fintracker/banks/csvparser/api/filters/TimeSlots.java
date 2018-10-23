package com.mariangolea.fintracker.banks.csvparser.api.filters;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public final class TimeSlots {

    private final Date start;
    private final Date end;

    public TimeSlots(final Date start, final Date end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Start date needs to be smaller or equals to end date!");
        }

        this.start = start;
        this.end = end;
    }

    public Collection<YearSlot> getMonthSlots() {
        Collection<YearSlot> intervals = Collections.EMPTY_LIST;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        calendar.setTime(end);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        if (startYear == endYear) {
            intervals.addAll(getMonthIntervals(startYear, startMonth, endMonth));
        } else {
            intervals.addAll(getMonthIntervals(startYear, startMonth, 11));
            for (int currentYear = startYear + 1; currentYear < endYear; currentYear++) {
                intervals.addAll(getMonthIntervals(currentYear, 0, 11));
            }
            intervals.addAll(getMonthIntervals(endYear, 0, endMonth));
        }

        return intervals;
    }

    public Collection<YearSlot> getYearSlots() {
        Collection<YearSlot> intervals = Collections.EMPTY_LIST;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(end);
        int endYear = calendar.get(Calendar.YEAR);
        
        if (startYear == endYear){
            intervals.add(new YearSlot(endYear));
        }
        for (int currentYear = startYear+1; currentYear <= endYear; currentYear++){
            intervals.add(new YearSlot(currentYear));
        }
        return intervals;
    }

    protected Collection<MonthSlot> getMonthIntervals(int year, int startMonth, int endMonth) {
        Collection<MonthSlot> intervals = Collections.EMPTY_LIST;
        for (int currentMonth = startMonth; currentMonth <= endMonth; currentMonth++) {
            intervals.add(new MonthSlot(currentMonth, year));
        }

        return intervals;
    }
}
