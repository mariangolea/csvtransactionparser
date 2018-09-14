package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base abstract class allowing grouping of transaction on criteria that
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
 *
 * @author mariangolea@gmail.com
 */
public abstract class BankTransactionAbstractGroup {

    private final String transactionGroupIdentifier;
    private final BankTransaction.Type type;
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * Create a instance of this class.
     *
     * @param transactionGroupIdentifier identifier for this group.
     * @param type whether it represents outgoing or incoming currency.
     */
    public BankTransactionAbstractGroup(final String transactionGroupIdentifier, BankTransaction.Type type) {
        Objects.requireNonNull(transactionGroupIdentifier);
        Objects.requireNonNull(type);

        this.transactionGroupIdentifier = transactionGroupIdentifier;
        this.type = type;
    }

    /**
     * See how many transactions are part of this group.
     *
     * @return
     */
    public abstract int getTransactionsNumber();
    
    /**
     * Each inheritor has its own data structure according to its transaction grouping criteria.
     * <br> This method is present to allow them do just that.
     * @param transaction transaction to add based on specific criteria.
     */
    protected abstract void addTransactionImpl(final BankTransaction transaction);

    /**
     * Check whether this transaction group accepts the possible match.
     * <br> verifies for equality of group identifier and type. 
     * <br> called automatically when an attempt to add a transaction to this group is made.
     * @param transaction transaction to verify.
     * @return 
     */
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
    
    /**
     * Get the total currency amount in contained transactions.
     * @return 
     */
    public BigDecimal getTotalAmount() {
        return amount;
    }

    /**
     * Whether this group represents outgoing or incoming transactions.
     * @return 
     */
    public BankTransaction.Type getType() {
        return type;
    }
    
    /**
     * Try to add a transaction to this group, returns true if successful, basically if {@link #matchesTransaction} returns true.
     * @param parsedTransaction transaction to add
     * @return true if successful
     */
    public final boolean addTransaction(final BankTransaction parsedTransaction) {
        if (matchesTransaction(parsedTransaction)) {
            addTransactionImpl(parsedTransaction);
            amount = amount.add(parsedTransaction.getAmount());
            return true;
        }
        return false;

    }
    
    /**
     * Try to add a list of transactions to this group.
     * @param parsedTransactions transactions to add
     * @return list of transaction which do not match this group.
     */
    public List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
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
        return type == that.type
                && Objects.equals(transactionGroupIdentifier, that.transactionGroupIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, transactionGroupIdentifier);
    }

    @Override
    public String toString() {
        return transactionGroupIdentifier + "\n" + amount.floatValue(); 
    }
}
