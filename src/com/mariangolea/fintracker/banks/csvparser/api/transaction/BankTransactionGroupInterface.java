package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.Collection;

public interface BankTransactionGroupInterface extends Comparable<BankTransactionGroupInterface>{

    public int getTransactionsNumber();

    public int getGroupsNumber();

    public String getCategoryName();

    public Collection<BankTransactionGroupInterface> getContainedGroups();

    public Collection<BankTransaction> getContainedTransactions();

    public BigDecimal getTotalAmount();
}
