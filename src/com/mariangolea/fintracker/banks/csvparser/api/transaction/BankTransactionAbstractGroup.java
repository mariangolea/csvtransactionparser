package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.Objects;

public abstract class BankTransactionAbstractGroup implements BankTransactionGroupInterface {

    private final String categoryName;

    public BankTransactionAbstractGroup(final String categoryName) {
        Objects.requireNonNull(categoryName);

        this.categoryName = categoryName;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int getTransactionsNumber(){
        return getContainedTransactions().size();
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
                + getTotalAmount().floatValue() + "\n" 
                + getTransactionsNumber() + " transactions" + "\n" 
                + getGroupsNumber() + " groups";
    }

    @Override
    public int compareTo(BankTransactionGroupInterface o) {
        return categoryName.compareTo(o.getCategoryName());
    }
}
