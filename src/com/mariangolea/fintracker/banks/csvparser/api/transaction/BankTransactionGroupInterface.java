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
 * Contains a list of transactions matching a specific user defined category, in
 * a general tree structure.
 * <br> For example: food=megaimage,auchan; clothing=zara,mango;
 * essentials=food, clothing; Class has a reference to
 * {@link UserPreferencesHandler} to get the user defined categories and for
 * each definition it stores the corresponding transaction groups.
 * <br> For this reasons, only the leafs of this tree will represent actual
 * {@link BankTransaction} objects.
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public interface BankTransactionGroupInterface {
    
    /**
     * User preferences handler.
     */
    public static final UserPreferencesHandler USER_PREFERENCES_HANDLER = UserPreferencesHandler.getInstance();

    /**
     * Get the total number of contained transactions.
     *
     * @return number of contained transactions
     */
    public int getTransactionsNumber();

    /**
     * Get the user defined category that this object represents.
     *
     * @return leafs will return null, in which case caller needs to
     */
    public String getUserDefinedCategory();

    public List<BankTransactionGroupInterface> getContainedTransactions();
    
    public BigDecimal getTotalAmount();
    
    public String getGroupIdentifier();
}
