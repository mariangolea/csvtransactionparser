package com.mariangolea.fintracker.banks.csvparser.api.filters;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class TimeSlots {
    private final Collection<BankTransaction> transactions;
    private final Set<YearSlot> monthSlots = new HashSet<>();
    private final Collection<YearSlot> yearSlots = new HashSet<>();

    public TimeSlots(final Collection<BankTransaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public Collection<YearSlot> getMonthSlots() {
        if (monthSlots.isEmpty()){
            computeSlots();
        }
        return monthSlots;
    }

    public Collection<YearSlot> getYearSlots() {
        if (yearSlots.isEmpty()){
            computeSlots();
        }
        return yearSlots;
    }

    private void computeSlots(){
        Calendar cal = Calendar.getInstance();
        transactions.forEach(transaction ->{
            cal.setTime(transaction.completedDate);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            monthSlots.add(new MonthSlot(month, year));
            yearSlots.add(new YearSlot(year));
        });
    }
}
