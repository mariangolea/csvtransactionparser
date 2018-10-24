package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Container of parsed and raw csv data for any transaction.
 * <br> Instances of this class will always contain data read directly from CSV
 * files, making no further changes on them.
 */
public final class BankTransaction implements Serializable, Comparable<BankTransaction> {

    public final Date startDate;
    public final Date completedDate;
    public final BigDecimal creditAmount;
    public final BigDecimal debitAmount;
    public final String description;

    private final List<String> csvContent = new ArrayList<>();

    public BankTransaction(
            final Date startDate,
            final Date completedDate,
            BigDecimal creditAmount,
            BigDecimal debitAmount,
            final String description,
            final List<String> csvContent) {
        super();
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(completedDate);
        Objects.requireNonNull(description);
        Objects.requireNonNull(csvContent);

        this.startDate = startDate;
        this.completedDate = completedDate;
        this.creditAmount = creditAmount == null ? BigDecimal.ZERO : creditAmount.abs();
        this.debitAmount = debitAmount == null ? BigDecimal.ZERO : debitAmount.abs();
        this.description = description;
        this.csvContent.addAll(csvContent);
    }

    public final List<String> getCsvContent() {
        return Collections.unmodifiableList(csvContent);
    }

    public final int getCSVContentLines() {
        return csvContent.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransaction that = (BankTransaction) o;
        return Objects.equals(that.creditAmount, creditAmount)
                && Objects.equals(that.debitAmount, debitAmount)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(completedDate, that.completedDate)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                startDate,
                completedDate,
                creditAmount,
                debitAmount,
                description);
    }

    @Override
    public int compareTo(final BankTransaction o) {
        int result = completedDate.compareTo(o.completedDate);
        if (result == 0){
            result = description.compareTo(o.description);
        }
        return result;
    }
    
}
