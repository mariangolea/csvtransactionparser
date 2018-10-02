package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Container of both parsed and raw csv data for any transaction.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransaction implements Serializable, BankTransactionGroupInterface {

    /**
     * Whether a certain {@link BankTransaction} represents a incoming or
     * outgoing operation.
     */
    public enum Type {
        IN, OUT
    }

    // Date when the transaction has been initiated (like on a POS).
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

    private boolean transactionTargetIdentified = false;
    private boolean validatedDuringParse = false;

    /**
     * Create a bank transaction instance.
     *
     * @param validatedDuringParse if parser supports strict validation (amounts
     * and company ID) and validation went OK.
     * @param transactionTargetIdentified if parser supports company string
     * identification and it was found.
     * @param title transaction type as a string.
     * @param startDate date when transaction has been initiated by the client.
     * @param completedDate date of transaction settlement.
     * @param amount currency amount
     * @param description transaction description (useful for a client to re
     * categorize a certain transaction).
     * @param type transaction type
     * @param csvContent original csv content.
     */
    public BankTransaction(boolean validatedDuringParse, boolean transactionTargetIdentified, final String title, final Date startDate,
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
        this.transactionTargetIdentified = transactionTargetIdentified;
        this.validatedDuringParse = validatedDuringParse;
    }

    @Override
    public int getTransactionsNumber() {
        return 0;
    }

    @Override
    public String getUserDefinedCategory() {
        return null;
    }

    @Override
    public List<BankTransactionGroupInterface> getContainedTransactions() {
        return null;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return amount;
    }

    @Override
    public String getGroupIdentifier() {
        return title;
    }
    
    /**
     * Whether transaction target (usually a company) was identified.
     * {@link  com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser#createCompanyIdDescriptionTransaction}
     *
     * @return
     */
    public boolean transactionTargetIdentified() {
        return transactionTargetIdentified;
    }

    /**
     * Whether the transaction was validated during parse.
     * {@link  com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser#createStrictDescriptionTransaction}
     *
     * @return true if {@link BankTransaction#transactionTargetIdentified}
     * returns true, and in case amount validation was supported, it also
     * returned true.
     */
    public boolean isValidatedDuringParse() {
        return validatedDuringParse;
    }

    /**
     * Get the Date object for when this transaction was initiated.
     *
     * @return
     */
    public final Date getStartDate() {
        return startDate;
    }

    /**
     * Get the Date object for when this transaction was settled by the bank.
     *
     * @return
     */
    public final Date getCompletedDate() {
        return completedDate;
    }

    /**
     * Amount of currency spent on this transaction.
     *
     * @return at least {@link BigDecimal#ZERO}
     */
    public final BigDecimal getAmount() {
        return amount;
    }

    /**
     * Every transaction contains a certain description.
     * <br> Each parser expects a hard coded list of transaction descriptions
     * for which it can translate this content to something more meaningful to
     * the user.
     *
     * @return meaningful adaptation (company string), or all description string
     * from CSV content when transaction identification fails or is obsolete.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Whether this is a outgoing or incoming currency transaction.
     *
     * @return
     */
    public final Type getType() {
        return type;
    }

    /**
     * Each parser is aware of a hard coded list of transaction "types" it knows
     * (POS operations, credit payments, and so on).
     * <br> Whenever that happens, title will contain a short string version
     * which makes sense to the user.
     * <br> Otherwise, it contains the full string found, allowing user to edit
     * it.
     *
     * @return
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Get the full list of String lines of CSV text that this transaction was
     * parsed from.
     *
     * @return
     */
    public final List<String> getCsvContent() {
        return csvContent;
    }

    /**
     * Get the number of CSV lines of text which were parsed for this
     * transaction.
     *
     * @return
     */
    public final int getOriginalCSVContentLinesNumber() {
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
        return Objects.equals(that.amount, amount)
                && transactionTargetIdentified == that.transactionTargetIdentified
                && validatedDuringParse == that.validatedDuringParse
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(completedDate, that.completedDate)
                && Objects.equals(description, that.description)
                && type == that.type
                && Objects.equals(title, that.title)
                && Objects.equals(csvContent, that.csvContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                validatedDuringParse,
                transactionTargetIdentified,
                startDate,
                completedDate,
                amount,
                description,
                type,
                title,
                csvContent);
    }
}
