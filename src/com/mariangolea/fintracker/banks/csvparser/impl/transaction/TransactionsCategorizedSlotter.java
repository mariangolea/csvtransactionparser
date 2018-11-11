package com.mariangolea.fintracker.banks.csvparser.impl.transaction;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.util.Pair;

/**
 * Produces a structured model from given list of parsed bank transactions and a
 * user preferences object.
 */
public class TransactionsCategorizedSlotter {

    private final UserPreferencesInterface userPrefs;
    private final List<BankTransaction> categorizableTransactions = new ArrayList<>();

    private final Map<YearSlot, Collection<BankTransactionGroupInterface>> slottedCategorised;
    private final Collection<BankTransaction> unCategorised;
    private final ObservableMap<Pair<YearSlot, String>, BankTransactionCompanyGroup> slottedCompanyGroups;
    private final TransactionsSlotter slotter;

    public TransactionsCategorizedSlotter(final Collection<BankTransaction> transactions, final UserPreferencesInterface userPrefs) {
        Objects.requireNonNull(transactions);
        Objects.requireNonNull(userPrefs);
        this.userPrefs = userPrefs;

        //get the collection of transactions which are not associated to any categories.
        unCategorised = findUncategorized(transactions);

        //keep only those currently associated to a category.
        this.categorizableTransactions.addAll(transactions);
        this.categorizableTransactions.removeAll(unCategorised);
        Collections.sort(categorizableTransactions);

        //create a slotted map of company grouped transactions.
        slotter = new TransactionsSlotter(userPrefs.getTransactionGroupingTimeframe(), this.categorizableTransactions);
        slottedCompanyGroups = categorizeByCompanyNames();
        slottedCategorised = slotAndCategorize();
    }

    public Map<YearSlot, Collection<BankTransactionGroupInterface>> getUnmodifiableSlottedCategorized() {
        return FXCollections.observableMap(slottedCategorised);
    }
    
    public Collection<BankTransaction> getUnmodifiableUnCategorized() {
        return FXCollections.observableArrayList(unCategorised);
    }

    protected Map<Pair<YearSlot, String>, BankTransactionCompanyGroup> getUnmodifiableSlottedCompanyGroups() {
        return FXCollections.unmodifiableObservableMap(slottedCompanyGroups);
    }

    protected final Map<YearSlot, Collection<BankTransactionGroupInterface>> slotAndCategorize() {
        Map<YearSlot, Collection<BankTransactionGroupInterface>> slotted = FXCollections.observableHashMap();
        Collection<String> topMostCategories = userPrefs.getTopMostCategories();
        topMostCategories.forEach((topMostCategory) -> {
            Map<YearSlot, BankTransactionGroupInterface> perSlot = createSlottedGroups(topMostCategory);
            perSlot.keySet().forEach(timeSlot -> {
                Collection<BankTransactionGroupInterface> collection = slotted.get(timeSlot);
                if (collection == null) {
                    collection = new ArrayList<>();
                    slotted.put(timeSlot, collection);
                }

                BankTransactionGroupInterface add = perSlot.get(timeSlot);
                if (add != null) {
                    collection.add(add);
                }
            });
        });
        return slotted;
    }
    
    protected final Map<YearSlot, BankTransactionGroupInterface> createSlottedGroups(final String category) {
        Collection<YearSlot> timeSlots = slotter.getTimeSlots();
        Map<YearSlot, BankTransactionGroupInterface> slotted = new ConcurrentHashMap<>();
        timeSlots.forEach(slot -> {
            BankTransactionGroupInterface group = createSlottedGroup(slot, category);
            if (group != null) {
                slotted.put(slot, group);
            }
        });

        return slotted;
    }

    protected final BankTransactionGroupInterface createSlottedGroup(final YearSlot timeSlot, final String category) {
        Collection<String> subCategories = userPrefs.getSubCategories(category);
        if (subCategories.isEmpty()) {
            return slottedCompanyGroups.get(new Pair(timeSlot, category));
        }

        final BankTransactionDefaultGroup group = new BankTransactionDefaultGroup(category);
        subCategories.forEach(subCategory -> {
            final BankTransactionGroupInterface subGroup = createSlottedGroup(timeSlot, subCategory);
            if (subGroup != null) {
                group.addGroup(subGroup);
            }
        });

        return group;
    }

    private ObservableMap<Pair<YearSlot, String>, BankTransactionCompanyGroup> categorizeByCompanyNames() {
        ObservableMap<Pair<YearSlot, String>, BankTransactionCompanyGroup> slottedCompanyGroupsLocal = FXCollections.observableHashMap();
        if (categorizableTransactions.isEmpty()) {
            return slottedCompanyGroupsLocal;
        }

        categorizableTransactions.forEach((transaction) -> {
            YearSlot timeSlot = slotter.getSlot(transaction.completedDate);
            String category = userPrefs.getMatchingCategory(transaction.description);
            Pair<YearSlot, String> key = new Pair(timeSlot, category);
            BankTransactionCompanyGroup group = slottedCompanyGroupsLocal.get(key);
            if (group == null) {
                group = new BankTransactionCompanyGroup(category);
                slottedCompanyGroupsLocal.put(key, group);
            }

            group.addTransaction(transaction);
        });

        return slottedCompanyGroupsLocal;
    }

    private Collection<BankTransaction> findUncategorized(final Collection<BankTransaction> transactions) {
        final List<BankTransaction> uncategorized = FXCollections.observableArrayList();
        transactions.forEach(transaction -> {
            String category = userPrefs.getMatchingCategory(transaction.description);
            if (UserPreferences.UNCATEGORIZED.equals(category)) {
                uncategorized.add(transaction);
            }
        });
        Collections.sort(uncategorized);

        return uncategorized;
    }
}
