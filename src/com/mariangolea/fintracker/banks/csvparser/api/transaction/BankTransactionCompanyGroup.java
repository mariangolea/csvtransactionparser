package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Group of transactions which were made with the same "target" entity.
 * @author mariangolea@gmail.com
 */
public final class BankTransactionCompanyGroup extends BankTransactionAbstractGroup {

    private final List<BankTransaction> list = new ArrayList<>();
    private final String companyDesc;
    
    /**
     * Create a instance of this class.
     * @param transactionGroupIdentifier group identifier
     * @param companyDesc company descriptor string
     * @param type outgoing or incoming
     */
    public BankTransactionCompanyGroup(String transactionGroupIdentifier, String companyDesc, BankTransaction.Type type) {
        super(transactionGroupIdentifier, type);
        Objects.requireNonNull(companyDesc);

        this.companyDesc = companyDesc;
    }
    
    @Override
    public int getTransactionsNumber() {
        return list.size();
    }

    @Override
    public void addTransactionImpl(final BankTransaction transaction) {
        list.add(transaction);
    }

    @Override
    public boolean matchesTransaction(final BankTransaction transaction) {
        return super.matchesTransaction(transaction) && Objects.equals(companyDesc, transaction.getDescription());
    }
    
    /**
     * Get the company descriptor string.
     * @return 
     */
    public String getCompanyDesc() {
        return companyDesc;
    }
    
    /**
     * Get a copy of the transactions as a list.
     * @return 
     */
    public List<BankTransaction> getTransactions() {
        return new ArrayList<>(list);
    }

    @Override
    public String toString() {
        return companyDesc + "\n" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransactionCompanyGroup that = (BankTransactionCompanyGroup) o;
        return super.equals(that)
                && Objects.equals(companyDesc, that.companyDesc) 
                && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), companyDesc, list);
    }

}
