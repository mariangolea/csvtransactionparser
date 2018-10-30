package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BankTransactionCompanyGroup extends BankTransactionAbstractGroup {

    private final ObservableList<BankTransaction> list = FXCollections.observableArrayList();
    private BigDecimal amount = BigDecimal.ZERO;

    public BankTransactionCompanyGroup(String companyDesc) {
        super(companyDesc);
        Objects.requireNonNull(companyDesc);
    }

    @Override
    public BigDecimal getTotalAmount() {
        return amount;
    }

    @Override
    public int getGroupsNumber() {
        return 0;
    }

    @Override
    public List<BankTransactionGroupInterface> getContainedGroups() {
        return null;
    }

    @Override
    public List<BankTransaction> getContainedTransactions() {
        return FXCollections.unmodifiableObservableList(list);
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
                && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), list);
    }

    protected void addTransaction(final BankTransaction parsedTransaction) {
        list.add(parsedTransaction);
        amount = amount.add(parsedTransaction.creditAmount).subtract(parsedTransaction.debitAmount);
    }

    protected void addTransactions(final Collection<BankTransaction> parsedTransactions) {
        parsedTransactions.forEach((transaction) -> {
            addTransaction(transaction);
        });
    }

}
