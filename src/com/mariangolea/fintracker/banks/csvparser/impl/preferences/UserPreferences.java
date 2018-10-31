package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class UserPreferences implements UserPreferencesInterface{

    private final CategoriesTree categories = new CategoriesTree();
    private String csvInputFolder;
    private Timeframe transactionsTimeframe = Timeframe.MONTH;
    //identifier string, display name.
    private final ObservableMap<String, String> companyNames = FXCollections.observableMap(new HashMap<>());
    //switched keys and values from previous map.
    private final ObservableMap<String, String> companyNamesReversed = FXCollections.observableMap(new HashMap<>());

    protected UserPreferences(){}
    
    @Override
    public Collection<String> getUserDefinedCategoryNames() {
        return categories.getAllSubCategoryNames();
    }

    @Override
    public Collection<String> getTopMostCategories() {
        return categories.getNodeSubCategoryNames();
    }

    @Override
    public Collection<String> getCompanyIdentifierStrings() {
        return companyNames.keySet();
    }

    @Override
    public Collection<String> getSubCategories(final String categoryName) {
        Objects.requireNonNull(categoryName);
        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null) {
            return FXCollections.emptyObservableList();
        }
        return tree.getNodeSubCategoryNames();
    }

    @Override
    public void setCompanyDisplayName(final String company, final String displayName) {
        Objects.requireNonNull(company);
        Objects.requireNonNull(displayName);
        companyNames.put(company.toLowerCase(), displayName);
        companyNamesReversed.put(displayName, company.toLowerCase());
    }

    @Override
    public String getCompanyDisplayName(final String company) {
        Objects.requireNonNull(company);
        return companyNames.get(company.toLowerCase());
    }

    @Override
    public Collection<String> getCompanyDisplayNames() {
        return FXCollections.observableSet(new HashSet<>(companyNames.values()));
    }

    @Override
    public String getMatchingCategory(String companyDescriptionString) {
        for (String companyIdentifier : getCompanyIdentifierStrings()) {
            if (companyDescriptionString.toLowerCase().contains(companyIdentifier.toLowerCase())) {
                return getCompanyDisplayName(companyIdentifier);
            }
        }

        return UNCATEGORIZED;
    }

    @Override
    public String getCompanyIdentifierString(final String companyDisplayName) {
        return companyNamesReversed.get(companyDisplayName);
    }

    @Override
    public void appendDefinition(final String categoryName, final Collection<String> subCategories) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(subCategories);

        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null) {
            categories.addSubCategories(Arrays.asList(categoryName));
            tree = categories.getCategory(categoryName);
        }
        final Collection<String> newCategories = FXCollections.observableArrayList();
        final Collection<CategoriesTree> existingCategories = FXCollections.observableArrayList();
        subCategories.forEach(subcategory -> {
            CategoriesTree existing = categories.getCategory(subcategory);
            if (existing == null) {
                newCategories.add(subcategory);
            } else {
                existingCategories.add(existing);
            }
        });
        tree.reparent(existingCategories);
        tree.addSubCategories(newCategories);
    }

    @Override
    public String getParent(final String categoryName) {
        CategoriesTree tree = categories.getCategory(categoryName);
        return tree == null ? null : tree.getParentCategory().categoryName;
    }

    @Override
    public String getCSVInputFolder() {
        return csvInputFolder; 
    }
 
    @Override
    public void setCSVInputFolder(final String csvInputFolder) {
        this.csvInputFolder = csvInputFolder;
    }

    @Override
    public Timeframe getTransactionGroupingTimeframe() {
        return transactionsTimeframe;
    }

    @Override
    public void setTransactionGroupingTimeframe(Timeframe timeframe) {
        this.transactionsTimeframe = timeframe;
    }
}
