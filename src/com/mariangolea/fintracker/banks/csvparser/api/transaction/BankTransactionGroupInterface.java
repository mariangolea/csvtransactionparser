/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.math.BigDecimal;
import java.util.List;

/**
 * Contains a list of transactions grouped together by means of automatic
 * grouping or as a result of a user choice to create arbitrary groups of
 * groups.
 * <br> For example: food=megaimage,auchan; clothing=zara,mango;
 * essentials=food, clothing;
 * <br>Class has a reference to {@link UserPreferencesHandler} to get the user
 * defined categories and for each definition it stores the corresponding
 * transaction groups.
 * <br> For this reasons, only the leafs of this tree will represent actual
 * {@link BankTransaction} objects.
 */
public interface BankTransactionGroupInterface {

    public static final String UNCATEGORIZED = "Uncategorized";
    public static final String UNDEFINED_COMPANY = "Undefined";

    public int getTransactionsNumber();

    public int getGroupsNumber();

    public String getCategoryName();

    public List<BankTransactionGroupInterface> getContainedGroups();

    public List<BankTransaction> getContainedTransactions();

    public BigDecimal getTotalAmount();
}
