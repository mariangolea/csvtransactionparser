package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.TimeSlots;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Produces a structured model from given list of parsed bank transactions and a
 * user preferences object.
 * <br>Accesses:
 * <ul><li>user selected time slot (whether splitting should be made based on
 * months or years) which can be used as rows in the table view of transactions.
 * <li>top most categories (can be used as columns in the table view of
 * transactions).
 * <li> list of parsed bank transactions.</ul>
 * <br> Produces a map where keys are time slots and values are a list of tree
 * like objects representing top most category currency operations.
 *
 */
public class TransactionCategorizer {

    private final UserPreferences userPrefs;
    private final List<BankTransaction> transactions = new ArrayList<>();

    //for every relevant time slot (monthly or yearly), keep a collection of company groups (each group has a list of transactions).
    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedCompanyGroups = new ConcurrentHashMap<>();
    //for every relevant time slot (monthly or yearly), keep a collection fully categorized transactions. each group represents a top most category name.
    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedCategorised = new ConcurrentHashMap<>();

    public TransactionCategorizer(final Collection<BankTransaction> transactions, final UserPreferences userPrefs) {
        this.transactions.addAll(transactions);
        this.userPrefs = userPrefs;
    }

    /**
     * @return keys are time slots (month or year). Values represent a
     * collection of top most categories each containing full tree like sub
     * categories and their transactions.
     */
    public Map<YearSlot, Collection<BankTransactionGroupInterface>> categorize() {
        if (slottedCategorised.isEmpty()) {
            categorizeByCompanyNames();
            Collection<String> topMostCategories = userPrefs.getTopMostCategories();

            topMostCategories.forEach((topMostCategory) -> {
                Map<YearSlot, BankTransactionGroupInterface> perSlot = createSlottedGroups(topMostCategory);
                perSlot.keySet().forEach(timeSlot -> {
                    Collection<BankTransactionGroupInterface> collection = slottedCategorised.get(timeSlot);
                    if (collection == null){
                        collection = new ArrayList<>();
                        slottedCategorised.put(timeSlot, collection);
                    }

                    BankTransactionGroupInterface add = perSlot.get(timeSlot);
                    if (add != null) {
                        collection.add(perSlot.get(timeSlot));
                    }
                });
            });
        }
        return slottedCategorised;
    }

    protected Map<YearSlot, BankTransactionGroupInterface> createSlottedGroups(final String category) {
        Collection<YearSlot> timeSlots = computeTimeSlots();
        Map<YearSlot, BankTransactionGroupInterface> slotted = new ConcurrentHashMap<>();
        timeSlots.forEach(slot -> {
            BankTransactionGroupInterface group = createSlottedGroup(slot, category);
            if (group != null) {
                slotted.put(slot, group);
            }
        });

        return slotted;
    }

    protected BankTransactionGroupInterface createSlottedGroup(final YearSlot timeSlot, final String category) {
        Collection<String> subCategories = userPrefs.getCategory(category);
        if (subCategories != null) {
            final BankTransactionDefaultGroup group = new BankTransactionDefaultGroup(category);
            subCategories.stream().map((subCategory) -> createSlottedGroup(timeSlot, subCategory)).forEachOrdered((subGroup) -> {
                if (subGroup != null) {
                    group.addGroup(subGroup);
                }
            });
            return group;
        } else {
            BankTransactionGroupInterface companyGroup = findCompanyGroup(timeSlot, category);
            return companyGroup;
        }
    }

    protected BankTransactionGroupInterface findCompanyGroup(final YearSlot timeSlot, final String companyName) {
        Collection<BankTransactionGroupInterface> slotedCompanyGroups = slottedCompanyGroups.get(timeSlot);
        if (slotedCompanyGroups == null) {
            return null;
        }

        for (BankTransactionGroupInterface companyGroup : slotedCompanyGroups) {
            if (companyGroup.getCategoryName().equalsIgnoreCase(companyName)) {
                return companyGroup;
            }
        }

        return null;
    }

    protected Map<YearSlot, Collection<BankTransactionGroupInterface>> categorizeByCompanyNames() {
        if (transactions.isEmpty()) {
            return slottedCompanyGroups;
        }

        if (slottedCompanyGroups.isEmpty()) {
            Collections.sort(transactions);

            final Map<YearSlot, Map<String, BankTransactionGroupInterface>> slottedCategoriesMap = new HashMap<>();
            transactions.forEach((transaction) -> {
                YearSlot timeSlot = createSlot(transaction.completedDate);
                Map<String, BankTransactionGroupInterface> categoriesMap = slottedCategoriesMap.get(timeSlot);
                if (categoriesMap == null) {
                    categoriesMap = new HashMap<>();
                    slottedCategoriesMap.put(timeSlot, categoriesMap);
                }

                String category = getMatchingCategory(transaction.description);
                BankTransactionGroupInterface group = categoriesMap.get(category);
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

            slottedCategoriesMap.entrySet().forEach(entry -> {
                slottedCompanyGroups.put(entry.getKey(), entry.getValue().values());
            });
        }
        return slottedCompanyGroups;
    }

    protected String getMatchingCategory(String transactionDescription) {
        for (String companyIdentifier : userPrefs.getCompanyIdentifierStrings()) {
            if (transactionDescription.toLowerCase().contains(companyIdentifier.toLowerCase())) {
                return userPrefs.getCompanyDisplayName(companyIdentifier);
            }
        }

        return BankTransactionGroupInterface.UNCATEGORIZED;
    }

    protected YearSlot createSlot(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        UserPreferences.Timeframe timeframe = userPrefs.getTransactionGroupingTimeframe();
        return UserPreferences.Timeframe.YEAR == timeframe ? new YearSlot(cal.get(Calendar.YEAR)) : new MonthSlot(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    protected Collection<YearSlot> computeTimeSlots() {
        TimeSlots timeSlotsCalc = new TimeSlots(transactions);
        UserPreferences.Timeframe timeframe = userPrefs.getTransactionGroupingTimeframe();
        return UserPreferences.Timeframe.YEAR == timeframe ? timeSlotsCalc.getYearSlots() : timeSlotsCalc.getMonthSlots();
    }
}
