package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Each transaction parser uses a set of hardcoded strings in order to identify
 * transactions. <br>
 * This class contains a list of all transactions which belong to a unique
 * "category" string, aka multiple instances of the same transaction type.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransactionGroup {

    private final List<BankTransaction> transactions = new ArrayList<>();
    private final String transactionGroupIdentifier;
    private final BankTransaction.Type type;

    /**
     * Crate a group of similar transactions for a specific bank.
     *
     * @param transactionGroupIdentifier group identifier (null not allowed)
     * @param type transaction type
     */
    public BankTransactionGroup(final String transactionGroupIdentifier,
            final BankTransaction.Type type) {
        Objects.requireNonNull(transactionGroupIdentifier);
        Objects.requireNonNull(type);

        this.transactionGroupIdentifier = transactionGroupIdentifier;
        this.type = type;
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

    /**
     * Add a specific bank transaction to the current group.
     *
     * @param parsedTransaction bank transaction (null not allowed)
     * @return true if successful: transaction is compatible to current group
     * (matches bank swift code and transaction title).
     */
    public final boolean addTransaction(final BankTransaction parsedTransaction) {
        if (matchesTransaction(parsedTransaction)) {
            transactions.add(parsedTransaction);
            return true;
        }
        return false;
    }

    /**
     * Add a list of compatible transactions.
     *
     * @param parsedTransactions list of compatible transactions
     * @return sub list of incompatible transactions (do not match title or bank
     * swift code).
     */
    public final List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
        List<BankTransaction> incompatibleTransactions = new ArrayList<>();
        for (BankTransaction transaction : parsedTransactions) {
            boolean added = addTransaction(transaction);
            if (!added) {
                incompatibleTransactions.add(transaction);
            }
        }
        return incompatibleTransactions;
    }

    public final List<BankTransaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public final BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (BankTransaction transaction : transactions) {
            amount = amount.add(transaction.getAmount());
        }

        return amount;
    }

    public final BankTransaction.Type getType() {
        return type;
    }

    public boolean matchesTransaction(final BankTransaction transaction) {
        return null != transaction && transactionGroupIdentifier.equals(transaction.getTitle())
                && type == transaction.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransactionGroup that = (BankTransactionGroup) o;
        return type == that.type
                && Objects.equals(transactions, that.transactions)
                && Objects.equals(transactionGroupIdentifier, that.transactionGroupIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, transactions, transactionGroupIdentifier);
    }

}
