package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Container class for all relevant fields of a specific bank transaction. This
 * is the container of raw csv data for any transaction.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransaction implements Serializable {

    public enum Type {
        IN, OUT
    }

    // Date when the transaction has been initiated by a client.
    private final Date startDate;
    // Date when the transaction has been settled by the bank. Same as startDate in
    // most cases.
    private final Date completedDate;
    // amount of currency used in this transaction.
    private final BigDecimal amount;
    // description of the transaction. useful for a client to recategorize a certain
    // transaction.
    private final String description;
    // type of transaction.
    private final Type type;
    // title as it appears in csv
    private final String title;

    // csv content lines read from csv file, before they were parsed.
    private final List<String> csvContent = new ArrayList<>();

    private boolean supportsTransactionCompanyIdentification = false;
    private boolean validatedDuringParse = false;

    /**
     * Constructs a transaction instance.
     * param supportsTransactionCompanyIdentification if desc string may be used to identify a certain company name
     *
     * @param validatedDuringParse                     if parser supported strict valiation (amounts and company ID) and validation went OK.
     * @param supportsTransactionCompanyIdentification if parser supported company string identification and vit was found.
     * @param title                                    transaction type as a string
     * @param startDate                                date when transaction has been initiated by the client.
     * @param completedDate                            date of transaction settlement.
     * @param amount                                   currency amount
     * @param description                              transaction description (useful for a client to re categorize a
     *                                                 certain transaction).
     * @param type                                     transaction type
     * @param csvContent                               original csv content.
     */
    public BankTransaction(boolean validatedDuringParse, boolean supportsTransactionCompanyIdentification, final String title, final Date startDate,
                           final Date completedDate, BigDecimal amount, final String description, final Type type,
                           final List<String> csvContent) {
        super();
        Objects.requireNonNull(title);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(completedDate);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(description);
        Objects.requireNonNull(type);
        Objects.requireNonNull(csvContent);

        this.title = title;
        this.startDate = startDate;
        this.completedDate = completedDate;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.csvContent.addAll(csvContent);
        this.supportsTransactionCompanyIdentification = supportsTransactionCompanyIdentification;
        this.validatedDuringParse = validatedDuringParse;
    }

    public boolean isSupportsTransactionCompanyIdentification() {
        return supportsTransactionCompanyIdentification;
    }

    public void setSupportsTransactionCompanyIdentification(boolean supportsTransactionCompanyIdentification) {
        this.supportsTransactionCompanyIdentification = supportsTransactionCompanyIdentification;
    }

    public void setValidatedDuringParse(boolean validatedDuringParse) {
        this.validatedDuringParse = validatedDuringParse;
    }

    public boolean isValidatedDuringParse() {
        return validatedDuringParse;
    }

    public final Date getStartDate() {
        return startDate;
    }

    public final Date getCompletedDate() {
        return completedDate;
    }

    public final BigDecimal getAmount() {
        return amount;
    }

    public final String getDescription() {
        return description;
    }

    public final Type getType() {
        return type;
    }

    public final String getTitle() {
        return title;
    }

    public final List<String> getCsvContent() {
        return csvContent;
    }

    public final int getOriginalCSVContentLinesNumber() {
        return csvContent.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Objects.equals(that.amount, amount) &&
                supportsTransactionCompanyIdentification == that.supportsTransactionCompanyIdentification &&
                validatedDuringParse == validatedDuringParse &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(completedDate, that.completedDate) &&
                Objects.equals(description, that.description) &&
                type == that.type &&
                Objects.equals(title, that.title) &&
                Objects.equals(csvContent, that.csvContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatedDuringParse, supportsTransactionCompanyIdentification, startDate, completedDate, amount, description, type, title, csvContent, supportsTransactionCompanyIdentification);
    }
}
