package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public abstract int getTransactionsNumber();

    protected void addTransactionImpl(final BankTransaction transaction) {
    }

    public boolean matchesTransaction(final BankTransaction transaction) {
        return null != transaction && transaction.description != null && transaction.description.contains(categoryName);
    }

    @Override
    public BigDecimal getTotalAmount() {
        return amount;
    }

    protected boolean addTransaction(final BankTransaction parsedTransaction) {
        if (matchesTransaction(parsedTransaction)) {
            addTransactionImpl(parsedTransaction);
            amount = amount.add(parsedTransaction.creditAmount).subtract(parsedTransaction.debitAmount);
            return true;
        }
        return false;

    }

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

    @Override
    public int compareTo(BankTransactionGroupInterface o) {
        return categoryName.compareTo(o.getCategoryName());
    }
}
