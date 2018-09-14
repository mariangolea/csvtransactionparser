/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Marian
 */
public abstract class BankTransactionAbstractGroup {

    private final String transactionGroupIdentifier;
    private final BankTransaction.Type type;
    private BigDecimal amount = BigDecimal.ZERO;

    public BankTransactionAbstractGroup(final String transactionGroupIdentifier, BankTransaction.Type type) {
        Objects.requireNonNull(transactionGroupIdentifier);
        Objects.requireNonNull(type);

        this.transactionGroupIdentifier = transactionGroupIdentifier;
        this.type = type;
    }

    protected abstract void addTransactionImpl(final BankTransaction transaction);

    public boolean matchesTransaction(final BankTransaction transaction) {
        return null != transaction && transactionGroupIdentifier.equals(transaction.getTitle())
                && type == transaction.getType();
    }

    /**
     * Get the group identifier (common transactions title as they show up in
     * csv).
     *
     * @return transactionGroupIdentifier
     */
    public final String getGroupIdentifier() {
        return transactionGroupIdentifier;
    }

    public BigDecimal getTotalAmount() {
        return amount;
    }
    
    public abstract int getTransactionsNumber();
    
    public BankTransaction.Type getType() {
        return type;
    }

    public final boolean addTransaction(final BankTransaction parsedTransaction) {
        if (matchesTransaction(parsedTransaction)) {
            addTransactionImpl(parsedTransaction);
            amount = amount.add(parsedTransaction.getAmount());
            return true;
        }
        return false;

    }

    public List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
        List<BankTransaction> incompatibleTransactions = new ArrayList<>();
        for (BankTransaction transaction : parsedTransactions) {
            boolean added = addTransaction(transaction);
            if (!added) {
                incompatibleTransactions.add(transaction);
            }
        }
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
        return type == that.type
                && Objects.equals(transactionGroupIdentifier, that.transactionGroupIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, transactionGroupIdentifier);
    }

    @Override
    public String toString() {
        return transactionGroupIdentifier + "\n" + amount.floatValue(); //To change body of generated methods, choose Tools | Templates.
    }
}
