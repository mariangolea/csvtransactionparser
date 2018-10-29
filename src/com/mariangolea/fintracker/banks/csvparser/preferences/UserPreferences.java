package com.mariangolea.fintracker.banks.csvparser.preferences;

import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class UserPreferences {

    public static final String UNCATEGORIZED = "Uncategorized";

    private final CategoriesTree categories = new CategoriesTree();
    private String csvInputFolder;
    private Timeframe transactionsTimeframe = Timeframe.MONTH;
    //identifier string, display name.
    private final ObservableMap<String, String> companyNames = FXCollections.observableMap(new HashMap<>());
    //switched keys and values from previous map.
    private final ObservableMap<String, String> companyNamesReversed = FXCollections.observableMap(new HashMap<>());

    public enum Timeframe {
        MONTH(Calendar.MONTH),
        YEAR(Calendar.YEAR);

        public final int timeframe;

        private Timeframe(int timeframe) {
            this.timeframe = timeframe;
        }
    }

    public Collection<String> getUserDefinedCategoryNames() {
        return categories.getAllSubCategoryNames();
    }

    public Collection<String> getTopMostCategories() {
        return categories.getNodeSubCategoryNames();
    }

    public Collection<String> getCompanyIdentifierStrings() {
        return companyNames.keySet();
    }

    public Collection<String> getSubCategories(final String categoryName) {
        Objects.requireNonNull(categoryName);
        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null){
            return FXCollections.emptyObservableList();
        }
        return tree.getNodeSubCategoryNames();
    }

    public void setCompanyDisplayName(final String company, final String displayName) {
        Objects.requireNonNull(company);
        Objects.requireNonNull(displayName);
        companyNames.put(company.toLowerCase(), displayName);
        companyNamesReversed.put(displayName, company.toLowerCase());
    }

    public String getCompanyDisplayName(final String company) {
        Objects.requireNonNull(company);
        return companyNames.get(company.toLowerCase());
    }

    public String getMatchingCategory(String companyDescriptionString) {
        for (String companyIdentifier : getCompanyIdentifierStrings()) {
            if (companyDescriptionString.toLowerCase().contains(companyIdentifier.toLowerCase())) {
                return getCompanyDisplayName(companyIdentifier);
            }
        }

        return UNCATEGORIZED;
    }

    public String getCompanyIdentifierString(final String companyDisplayName) {
        return companyNamesReversed.get(companyDisplayName);
    }

    public void appendDefinition(final String categoryName, final Collection<String> subCategories) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(subCategories);
        
        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null){
            categories.addSubCategories(Arrays.asList(categoryName));
            tree = categories.getCategory(categoryName);
        }
        final Collection<String> newCategories = FXCollections.observableArrayList();
        final Collection<CategoriesTree> existingCategories = FXCollections.observableArrayList();
        subCategories.forEach(subcategory ->{
            CategoriesTree existing = categories.getCategory(subcategory);
            if (existing == null){
                newCategories.add(subcategory);
            } else{
                existingCategories.add(existing);
            }
        });
        tree.reparent(existingCategories);
        tree.addSubCategories(newCategories);
    }

    public String getParent(final String categoryName) {
        CategoriesTree tree = categories.getCategory(categoryName);
        return tree == null ? null : tree.getParentCategory().categoryName;
    }

    public String getCSVInputFolder() {
        return csvInputFolder;
    }

    public void setCSVInputFolder(final String csvInputFolder) {
        this.csvInputFolder = csvInputFolder;
    }

    public Timeframe getTransactionGroupingTimeframe() {
        return transactionsTimeframe;
    }

    public void setTransactionGroupingTimeframe(Timeframe timeframe) {
        this.transactionsTimeframe = timeframe;
    }
}
