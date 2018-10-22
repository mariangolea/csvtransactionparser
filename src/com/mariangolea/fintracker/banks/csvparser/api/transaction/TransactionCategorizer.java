package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements a custom chain of user defined categorising criteria.
 */
public class TransactionCategorizer {

    private final UserPreferences userPrefs;
    private final List<BankTransaction> transactions = new ArrayList<>();
    private final Collection<BankTransactionCompanyGroup> companyGroups = Collections.EMPTY_LIST;
    private final Collection<BankTransactionGroupInterface> categorised = Collections.EMPTY_LIST;

    public TransactionCategorizer(final List<BankTransaction> transactions, final UserPreferences userPrefs) {
        this.transactions.addAll(transactions);
        this.userPrefs = userPrefs;
    }

    public Collection<BankTransactionGroupInterface> categorize() {
        if (categorised.isEmpty()) {
            companyGroups.addAll(categorizeByCompanyNamesFromUserPrefs());
            Collection<String> topMostCategories = userPrefs.getTopMostCategories();
            for (String topMostCategory : topMostCategories) {
                categorised.add(createGroup(topMostCategory));
            }
        }
        return categorised;
    }

    protected BankTransactionGroupInterface createGroup(final String category) {
        Collection<String> subCategories = userPrefs.getCategory(category);
        if (subCategories != null) {
            final BankTransactionDefaultGroup group = new BankTransactionDefaultGroup(category);
            subCategories.stream().map((subCategory) -> createGroup(subCategory)).forEachOrdered((subGroup) -> {
                group.addGroup(subGroup);
            });
            return group;
        } else {
            BankTransactionCompanyGroup companyGroup = findCompanyGroup(category);
            companyGroups.remove(companyGroup);
            return companyGroup;
        }
    }

    protected BankTransactionCompanyGroup findCompanyGroup(final String companyDisplayName){
        for (BankTransactionCompanyGroup companyGroup : companyGroups){
            if (companyGroup.getCategoryName().equalsIgnoreCase(companyDisplayName)){
                return companyGroup;
            }
        }
        
        return null;
    }
    
    protected Collection<BankTransactionCompanyGroup> categorizeByCompanyNamesFromUserPrefs() {
        final Map<String, BankTransactionCompanyGroup> categoriesMap = new HashMap<>();

        //populate a map of categories and associated transactions.
        transactions.forEach((transaction) -> {
            String category = getMatchingCategory(transaction);
            if (category == null) {
                category = BankTransactionGroupInterface.UNCATEGORIZED;
            }
            BankTransactionCompanyGroup group = categoriesMap.get(category);
            if (group == null) {
                group = new BankTransactionCompanyGroup(category);
                categoriesMap.put(category, group);
            }

            List<BankTransaction> foundTransactions = group.getContainedTransactions();
            if (foundTransactions == null) {
                foundTransactions = new ArrayList<>();
            }
            foundTransactions.add(transaction);
        });

        return categoriesMap.values();
    }

    protected String getMatchingCategory(final BankTransaction transaction) {
        for (String companyIdentifier : userPrefs.getCompanyIdentifierStrings()) {
            if (transaction.description.toLowerCase().contains(companyIdentifier.toLowerCase())) {
                return userPrefs.getCompanyDisplayName(companyIdentifier);
            }
        }

        return BankTransactionGroupInterface.UNDEFINED_COMPANY;
    }

}
