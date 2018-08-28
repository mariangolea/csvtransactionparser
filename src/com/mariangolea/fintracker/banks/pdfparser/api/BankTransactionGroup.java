package com.mariangolea.fintracker.banks.pdfparser.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Each transaction parser uses a set of hardcoded strings in order to identify
 * transactions.
 * <br> This class contains a list of all transactions which belong to a unique
 * "category" string.
 *
 * @author mariangolea@gmail.com
 */
public class BankTransactionGroup {

    private final List<BankTransaction> transactions = new ArrayList<>();
    private final String transactionGroupIdentifier;

    public BankTransactionGroup(final String transactionGroupIdentifier) {
        this.transactionGroupIdentifier = transactionGroupIdentifier;
    }

    public BankTransactionGroup(final String transactionGroupIdentifier, List<BankTransaction> transactions) {
        this.transactionGroupIdentifier = transactionGroupIdentifier;
        this.transactions.addAll(transactions);
    }

    public String getTitle() {
        return transactionGroupIdentifier;
    }

    public boolean addTransaction(final BankTransaction parsedTransaction) {
        if (!transactionGroupIdentifier.equals(parsedTransaction.title)) {
            return false;
        }

        transactions.add(parsedTransaction);
        return true;
    }

    public List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
        List<BankTransaction> incompatibleTransactions = new ArrayList<>();
        for (BankTransaction transaction : parsedTransactions) {
            if (!transactionGroupIdentifier.equals(transaction.title)) {
                incompatibleTransactions.add(transaction);
            } else{
                transactions.add(transaction);
            }
        }
        return incompatibleTransactions;
    }

    public List<BankTransaction> getTransactions() {
        return transactions;
    }

    public double getTotalAmount() {
        double amount = 0;
        for (BankTransaction transaction : transactions) {
            amount += transaction.amount;
        }

        return amount;
    }

    public BankTransaction.Type getType() {
        if (transactions.isEmpty()) {
            return null;
        }

        return transactions.get(0).type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BankTransactionGroup)) {
            return false;
        }
        BankTransactionGroup other = (BankTransactionGroup) obj;

        if (!transactionGroupIdentifier.equals(other.transactionGroupIdentifier)) {
            return false;
        }

        if (transactions.size() != other.transactions.size()) {
            return false;
        }

        return transactions.stream().noneMatch((transaction) -> (!other.transactions.contains(transaction)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.transactions);
        hash = 59 * hash + Objects.hashCode(this.transactionGroupIdentifier);
        return hash;
    }

}
