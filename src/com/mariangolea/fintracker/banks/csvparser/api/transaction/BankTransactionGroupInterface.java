package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.math.BigDecimal;
import java.util.List;

public interface BankTransactionGroupInterface {

    public static final String UNCATEGORIZED = "Uncategorized";

    public int getTransactionsNumber();

    public int getGroupsNumber();

    public String getCategoryName();

    public List<BankTransactionGroupInterface> getContainedGroups();

    public List<BankTransaction> getContainedTransactions();

    public BigDecimal getTotalAmount();
}
