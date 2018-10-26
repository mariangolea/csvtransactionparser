package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.TimeSlots;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.collections.FXCollections;

public class TransactionsSlotter {

    private final Collection<BankTransaction> transactions;
    private final UserPreferences.Timeframe timeFrame;
    private final Map<Date, YearSlot> dateSlotsMap = FXCollections.observableHashMap();
    private final Collection<YearSlot> allTransactionsDateSlots;

    public TransactionsSlotter(UserPreferences.Timeframe timeFrame, final Collection<BankTransaction> transactions) {
        Objects.requireNonNull(timeFrame);
        Objects.requireNonNull(transactions);
        this.timeFrame = timeFrame;
        this.transactions = transactions;
        allTransactionsDateSlots = computeTimeSlots();
    }

    public YearSlot getSlot(final Date date) {
        YearSlot slot = dateSlotsMap.get(date);
        if (slot == null) {
            slot = createSlot(date);
            dateSlotsMap.put(date, slot);
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
        return UserPreferences.Timeframe.YEAR == timeframe ? new YearSlot(cal.get(Calendar.YEAR)) : new MonthSlot(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }
}
