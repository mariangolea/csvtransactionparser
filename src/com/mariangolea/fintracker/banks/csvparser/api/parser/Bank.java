package com.mariangolea.fintracker.banks.csvparser.api.parser;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public abstract class Bank {

    public final Locale locale;

    /**
     * Each CSV file has a header that the parser needs to skip when looking for
     * transactions.
     * <br>This represents a perfect match of what the header line string looks
     * like in a CSV report file for this bank.
     */
    public final String relevantContentHeaderLine;
    /**
     * Some banks add CSV content to specify the generated transactions number.
     * <br> If a certain bank does not provide one, this field will be a empty
     * string.
     * <br> It represents the label that associated parser needs to look for in
     * order to next parse the transactions number, for validation purposes.
     */
    public final String transactionsNumberLabel;

    public final NumberFormat numberFormat;
    public final DateFormat startDateFormat;
    public final DateFormat completedDateFormat;
    public final int[] mandatoryRecordIndexes;
    public final int maxRecordSize;

    public Bank(final Locale locale,
            final String relevantContentHeaderLine,
            final String transactionsNumberLabel, 
            final DateFormat startDateFormat, 
            final DateFormat completedDateFormat, 
            final NumberFormat numberFormat, 
            final int[] mandatoryRecordIndexes, 
            int maxRecordSize) {
        this.locale = Objects.requireNonNull(locale);
        this.relevantContentHeaderLine = Objects.requireNonNull(relevantContentHeaderLine);
        this.transactionsNumberLabel = Objects.requireNonNull(transactionsNumberLabel);
        this.startDateFormat = Objects.requireNonNull(startDateFormat);
        this.numberFormat = Objects.requireNonNull(numberFormat);
        this.completedDateFormat = Objects.requireNonNull(completedDateFormat);
        this.mandatoryRecordIndexes = Objects.requireNonNull(mandatoryRecordIndexes);
        this.maxRecordSize = maxRecordSize;
    }
}
