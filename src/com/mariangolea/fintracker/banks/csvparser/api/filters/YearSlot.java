package com.mariangolea.fintracker.banks.csvparser.api.filters;

public class YearSlot implements Comparable<YearSlot> {

    public final int year;

    public YearSlot(int year) {
        this.year = year;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.year;
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
        final YearSlot other = (YearSlot) obj;
        return year == other.year;
    }

    @Override
    public int compareTo(YearSlot o) {
        return year - o.year;
    }
}