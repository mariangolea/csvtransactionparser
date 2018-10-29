package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.TimeSlots;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javafx.collections.FXCollections;

public class TransactionsSlotter {

    private final Collection<BankTransaction> transactions;
    private final UserPreferences.Timeframe timeFrame;
    private final Map<DateKey, YearSlot> dateSlotsMap = FXCollections.observableHashMap();
    private final Collection<YearSlot> allTransactionsDateSlots;

    public TransactionsSlotter(UserPreferences.Timeframe timeFrame, final Collection<BankTransaction> transactions) {
        Objects.requireNonNull(timeFrame);
        Objects.requireNonNull(transactions);
        this.timeFrame = timeFrame;
        this.transactions = transactions;
        allTransactionsDateSlots = computeTimeSlots();
    }

    public YearSlot getSlot(final Date date) {
        DateKey key = new DateKey(date);
        YearSlot slot = dateSlotsMap.get(key);
        if (slot == null) {
            slot = createSlot(date);
            dateSlotsMap.put(key, slot);
        }

        return slot;
    }

    public Collection<YearSlot> getTimeSlots() {
        return allTransactionsDateSlots;
    }

    private Collection<YearSlot> computeTimeSlots() {
        TimeSlots timeSlotsCalc = new TimeSlots(transactions);
        UserPreferences.Timeframe timeframe = timeFrame;
        return UserPreferences.Timeframe.YEAR == timeframe ? timeSlotsCalc.getYearSlots() : timeSlotsCalc.getMonthSlots();
    }

    private YearSlot createSlot(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        UserPreferences.Timeframe timeframe = timeFrame;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return UserPreferences.Timeframe.YEAR == timeframe ? new YearSlot(year) : new MonthSlot(month, year);
    }

    private static class DateKey {

        final int year;
        final int month;
        final int day;

        public DateKey(final Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + this.year;
            hash = 37 * hash + this.month;
            hash = 37 * hash + this.day;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DateKey other = (DateKey) obj;
            if (this.year != other.year) {
                return false;
            }
            if (this.month != other.month) {
                return false;
            }
            return this.day == other.day;
        }
    }
}
