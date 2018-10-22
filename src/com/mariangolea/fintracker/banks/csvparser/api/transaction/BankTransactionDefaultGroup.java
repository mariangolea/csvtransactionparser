package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Each transaction parser uses a set of hard coded strings in order to identify
 * transactions. <br>
 * This class contains a map of all found transactions during the parse of a set
 * of CSV files. Key is represented by the parsed company descriptor.
 */
public class BankTransactionDefaultGroup extends BankTransactionCompanyGroup {

    private final List<BankTransactionGroupInterface> groups = new ArrayList<>();

    public BankTransactionDefaultGroup(final String category) {
        super(category);
    }

    @Override
    public List<BankTransactionGroupInterface> getContainedGroups() {
        return Collections.unmodifiableList(groups);
    }

    protected void addGroup(final BankTransactionGroupInterface group){
        groups.add(group);
        addTransactions(group.getContainedTransactions());
    }
    
    @Override
    public int getTransactionsNumber() {
        int number = 0;
        number = groups.stream().map((group) -> group.getTransactionsNumber()).reduce(number, Integer::sum);
        return number;
    }

    @Override
    public int getGroupsNumber() {
        return groups.size();
    }
    
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransactionDefaultGroup that = (BankTransactionDefaultGroup) o;
        return super.equals(that)
                && Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groups);
    }
}
