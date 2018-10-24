package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankTransactionCompanyGroup extends BankTransactionAbstractGroup {

    private final List<BankTransaction> list = new ArrayList<>();

    public BankTransactionCompanyGroup(String companyDesc) {
        super(companyDesc);
        Objects.requireNonNull(companyDesc);
    }

    @Override
    protected void addTransactionImpl(BankTransaction transaction) {
        list.add(transaction);
    }

    @Override
    public int getTransactionsNumber() {
        return list.size();
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
        return list;
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
}
