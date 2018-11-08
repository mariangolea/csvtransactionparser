package com.mariangolea.fintracker.banks.csvparser.impl.transaction;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankTransactionDefaultGroup extends BankTransactionCompanyGroup {

    private final List<BankTransactionGroupInterface> groups = new ArrayList<>();

    public BankTransactionDefaultGroup(final String category) {
        super(category);
    }

    @Override
    public List<BankTransactionGroupInterface> getContainedGroups() {
        return groups;
    }

    protected void addGroup(final BankTransactionGroupInterface group) {
        groups.add(group);
    }

    @Override
    public int getTransactionsNumber() {
        if (getGroupsNumber() == 0) {
            return super.getTransactionsNumber();
        } else {
            int number = 0;
            for (BankTransactionGroupInterface temp : groups) {
                number += temp.getTransactionsNumber();
            }
            return number;
        }
    }

    @Override
    public BigDecimal getTotalAmount() {
        if (getGroupsNumber() == 0) {
            return super.getTotalAmount();
        } else {
            BigDecimal number = BigDecimal.ZERO;
            for (BankTransactionGroupInterface temp : groups) {
                number = number.add(temp.getTotalAmount());
            }
            return number;
        }
    }

    @Override
    public int getGroupsNumber() {
        return groups.size();
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)){
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransactionDefaultGroup that = (BankTransactionDefaultGroup) o;
        return Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groups);
    }
}
