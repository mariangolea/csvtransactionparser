package com.mariangolea.fintracker.banks.pdfparser.preferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Container of user preferences.
 *
 * @author mariangolea@gmail.com
 */
public class UserPreferences {

    private final Map<String, List<String>> transactionCategories = new HashMap<>();
    private String pdfInputFolder;

    /**
     * Adds or updates (if category name already exists) a category name and its
     * associated transaction names.
     *
     * @param categoryName category name
     * @param transactionNames associated transaction names
     */
    public void addUpdateCategory(final String categoryName, final List<String> transactionNames) {
        List<String> existingAssociations = transactionCategories.get(categoryName);
        if (existingAssociations == null) {
            transactionCategories.put(categoryName, transactionNames);
        } else {
            transactionNames.forEach(transactionName -> {
                if (!existingAssociations.contains(transactionName)) {
                    existingAssociations.add(transactionName);
                }
            });
        }
    }

    public void removeCategory(final String categoryName) {
        transactionCategories.remove(categoryName);
    }

    public List<String> getCategory(final String categoryName) {
        return transactionCategories.get(categoryName);
    }

    public Map<String, List<String>> getAllCategories() {
        return transactionCategories;
    }

    public String getPDFInputFolder() {
        return pdfInputFolder;
    }

    public void setPDFInputFolder(final String pdfInputFolder) {
        this.pdfInputFolder = pdfInputFolder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserPreferences)) {
            return false;
        }
        UserPreferences other = (UserPreferences) obj;

        if (!pdfInputFolder.equals(other.pdfInputFolder)) {
            return false;
        }

        if (transactionCategories.size() != other.transactionCategories.size()) {
            return false;
        }

        for (String key : transactionCategories.keySet()) {
            List<String> firstAssociations = transactionCategories.get(key);
            List<String> secondAssociations = other.transactionCategories.get(key);
            if (!areListsEqual(firstAssociations, secondAssociations)){
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.transactionCategories);
        hash = 29 * hash + Objects.hashCode(this.pdfInputFolder);
        return hash;
    }

    private boolean areListsEqual(List<String> first, List<String> second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        if (first.size() != second.size()) {
            return false;
        }

        return first.containsAll(second);
    }
}
