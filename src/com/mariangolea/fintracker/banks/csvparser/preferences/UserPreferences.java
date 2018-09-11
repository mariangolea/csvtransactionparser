package com.mariangolea.fintracker.banks.csvparser.preferences;

import java.util.*;

/**
 * Container of user preferences:
 * <br>User preferred input folder.</br>
 * <br>User defined transaction categories, as a map. Category name as key, {@link UserDefinedTransactionGroup} as value.<br/>
 * <br>User defined transaction display names, as a map. Substring of a transaction description as a key, user defined display name as value.</br>
 *
 * @author mariangolea@gmail.com
 */
public class UserPreferences {

    private final Map<String, UserDefinedTransactionGroup> transactionCategories = new HashMap<>();
    private String csvInputFolder;
    private final Map<String, String> transactionDisplayNames = new HashMap<>();

    /**
     * Get a ordered set of available user defined categories.
     *
     * @return user defined categories
     */
    public Set<String> getUserDefinedCategoryNames() {
        return transactionCategories.keySet();
    }

    /**
     * Get a ordered set of available transaction description substrings that
     * may be used to identify a specific user defined company name.
     *
     * @return user defined transaction description substrings that
     * may be used to identify a specific user defined company name.
     */
    public Set<String> getUserDefinedCompanyNames() {
        return transactionDisplayNames.keySet();
    }


    /**
     * Get the user defined transaction group associated to specified user defined category name.
     *
     * @param categoryName user defined category name.
     * @return transaction group
     */
    public UserDefinedTransactionGroup getDefinition(final String categoryName) {
        Objects.requireNonNull(categoryName);
        return transactionCategories.get(categoryName);
    }

    /**
     * Set a user defined display name corresponding to a unique sub string found in a transaction description.
     * <br>This association is used to automatically group transactions done to the same company, so please be careful to make the
     * first argument as specific as possible.</br>
     * <br>A good example is providing simple strings like "LIDL" or "SC NEPTUN SA".</br>
     * <br> Avoid using POS identification numbers since these tend to change quite often in Romania.</br>
     *
     * @param transactionDesc
     * @param displayName
     */
    public void setTransactionDisplayName(final String transactionDesc, final String displayName) {
        Objects.requireNonNull(transactionDesc);
        Objects.requireNonNull(displayName);
        transactionDisplayNames.put(transactionDesc, displayName);
    }

    /**
     * Get the display name corresponding to received transaction description relevant sub string.
     * <br>A good example is providing simple strings like "LIDL" or "SC NEPTUN SA".</br>
     * <br> Avoid using POS identification numbers since these tend to change quite often in Romania.</br>
     *
     * @param transactionDesc transaction description relevant sub string
     * @return associated display name, may be null
     */
    public String getDisplayName(final String transactionDesc) {
        Objects.requireNonNull(transactionDesc);
        return transactionDisplayNames.get(transactionDesc);
    }


    /**
     * Add a user defined category name for corresponding transaction group.
     *
     * @param categoryName  user defined category name
     * @param newDefinition user defined transaction group.
     * @return true if successful. false if one association already exists (calls updateDefinition instead).
     */
    public boolean addDefinition(final String categoryName, final UserDefinedTransactionGroup newDefinition) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(newDefinition);
        if (transactionCategories.containsKey(categoryName)) {
            return false;
        }

        transactionCategories.put(categoryName, newDefinition);
        return true;
    }

    /**
     * Remove the existing definition for a specific user defined category name.
     *
     * @param categoryName user defined category name.
     * @return true if successful. false if no such association exists.
     */
    public boolean removeDefinition(final String categoryName) {
        Objects.requireNonNull(categoryName);
        if (!transactionCategories.containsKey(categoryName)) {
            return false;
        }
        transactionCategories.remove(categoryName);
        return true;
    }

    /**
     * Update the existing definition for a specific user defined category name.
     *
     * @param categoryName  user defined category name.
     * @param newDefinition user defined transaction group.
     * @return true if successful. false if no previous association existed.
     */
    public boolean updateDefinition(final String categoryName, final UserDefinedTransactionGroup newDefinition) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(newDefinition);
        if (!transactionCategories.containsKey(categoryName)) {
            return false;
        }
        transactionCategories.put(categoryName, newDefinition);
        return true;
    }

    /**
     * Get the list of existing user defined transaction groups.
     *
     * @return list of existing user defined transaction groups.
     */
    public Collection<UserDefinedTransactionGroup> getUserDefinitions() {
        return transactionCategories.values();
    }

    /**
     * Get the user defined input folder for parsing csv files.
     *
     * @return may be null.
     */
    public String getCSVInputFolder() {
        return csvInputFolder;
    }

    /**
     * Set the user defined input folder for parsing pd files.
     *
     * @param csvInputFolder csv input folder.
     */
    public void setCSVInputFolder(final String csvInputFolder) {
        this.csvInputFolder = csvInputFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPreferences that = (UserPreferences) o;
        return Objects.equals(transactionCategories, that.transactionCategories) &&
                Objects.equals(csvInputFolder, that.csvInputFolder) &&
                Objects.equals(transactionDisplayNames, that.transactionDisplayNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionCategories, csvInputFolder, transactionDisplayNames);
    }
}
