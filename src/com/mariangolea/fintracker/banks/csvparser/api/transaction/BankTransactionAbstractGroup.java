package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base abstract class allowing grouping of transactions on criteria that
 * inheritors need to support themselves.
 * <p>
 * The application allows multiple levels of grouping:
 * <br> 1. At the lowest level transactions are grouped based only on a single
 * file for a single bank.
 * <br>1.1 if company is not identified, transactions from the same bank are
 * grouped by transaction types (POS operations, credit, and so on)
 * {@link  BankTransactionDefaultGroup}.
 * <br>1.2. if company is identified, all transactions for a specific bank made
 * for this company are grouped together {@link  BankTransactionCompanyGroup}.
 * <br>2. If user has provided CSV reports from multiple banks, groups of the
 * same type are merged together on the same principle
 * {@link BankTransactionUtils#processTransactions}
 * <br>3. User then has the option to define his own "categories" like
 * "Food","Utilities", and so on. This latter one does not inherit from this
 * class, but it makes sense to mention it in here.
 * <br>4. User can also create larger groups out of the ones from point 3.
 * </p>
 */
public abstract class BankTransactionAbstractGroup implements BankTransactionGroupInterface {

    private final String categoryName;
    private BigDecimal amount = BigDecimal.ZERO;

    public BankTransactionAbstractGroup(final String categoryName) {
        Objects.requireNonNull(categoryName);

        this.categoryName = categoryName;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * See how many transactions are part of this group.
     *
     * @return
     */
    @Override
    public abstract int getTransactionsNumber();

    /**
     * Each inheritor has its own data structure according to its transaction
     * grouping criteria.
     * <br> This method is present to allow them do just that.
     *
     * @param transaction transaction to add based on specific criteria.
     */
    protected void addTransactionImpl(final BankTransaction transaction) {
    }

    public boolean matchesTransaction(final BankTransaction transaction) {
        return null != transaction && transaction.description != null && transaction.description.contains(categoryName);
    }

    @Override
    public BigDecimal getTotalAmount() {
        return amount;
    }

    /**
     * Try to add a transaction to this group, returns true if successful,
     * basically if {@link #matchesTransaction} returns true.
     *
     * @param parsedTransaction transaction to add
     * @return true if successful
     */
    protected boolean addTransaction(final BankTransaction parsedTransaction) {
        if (matchesTransaction(parsedTransaction)) {
            addTransactionImpl(parsedTransaction);
            amount = amount.add(parsedTransaction.creditAmount).subtract(parsedTransaction.debitAmount);
            return true;
        }
        return false;

    }

    /**
     * Try to add a list of transactions to this group.
     *
     * @param parsedTransactions transactions to add
     * @return list of transaction which do not match this group.
     */
    protected List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
        List<BankTransaction> incompatibleTransactions = new ArrayList<>();
        parsedTransactions.forEach((transaction) -> {
            boolean added = addTransaction(transaction);
            if (!added) {
                incompatibleTransactions.add(transaction);
            }
        });
        return incompatibleTransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof BankTransactionAbstractGroup)) {
            return false;
        }
        BankTransactionAbstractGroup that = (BankTransactionAbstractGroup) o;
        return Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

    @Override
    public String toString() {
        return categoryName + "\n" 
                + amount.floatValue() + "\n" 
                + getTransactionsNumber() + " transactions" + "\n" 
                + getGroupsNumber() + " groups";
    }
}
