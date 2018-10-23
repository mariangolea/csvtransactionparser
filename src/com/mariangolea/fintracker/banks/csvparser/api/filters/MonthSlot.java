package com.mariangolea.fintracker.banks.csvparser.api.filters;

public class MonthSlot extends YearSlot {

    public final int month;

    public MonthSlot(int month, int year) {
        super(year);
        this.month = month;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + this.month;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final MonthSlot other = (MonthSlot) obj;
        return this.month == other.month;
    }

    @Override
    public int compareTo(YearSlot o) {
        int yearComparison = super.compareTo(o);
        if (yearComparison != 0 || getClass() != o.getClass()) {
            return yearComparison;
        }

        final MonthSlot other = (MonthSlot) o;
        return month - other.month;
    }

}
