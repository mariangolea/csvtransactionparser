package com.mariangolea.fintracker.banks.csvparser.preferences;

import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class UserPreferences {

    private final ObservableMap<String, Collection<String>> categories = FXCollections.observableMap(new HashMap<>());
    private final Collection<String> topMostCategories = new ArrayList<>();
    private String csvInputFolder;
    private Timeframe transactionsTimeframe = Timeframe.MONTH;
    //identifier string, display name.
    private final ObservableMap<String, String> companyNames = FXCollections.observableMap(new HashMap<>());
    //switched keys and values from previous map.
    private final ObservableMap<String, String> companyNamesReversed = FXCollections.observableMap(new HashMap<>());

    public enum Timeframe{
        MONTH(Calendar.MONTH),
        YEAR(Calendar.YEAR);
        
        public final int timeframe;
        
        private Timeframe(int timeframe){
            this.timeframe = timeframe;
        }
        
        public static Timeframe getTimeframe(int timeframe){
            switch (timeframe){
                case 1:
                default:
                    return Timeframe.MONTH;
                case 2:
                    return Timeframe.YEAR;
            }
        }
    }
    
    public boolean addTransactionCategoriesMapListener(MapChangeListener<String, Collection<String>> listener) {
        if (listener == null) {
            return false;
        }

        categories.addListener(listener);
        return true;
    }

    public boolean removeTransactionCategoriesMapListener(MapChangeListener<String, Collection<String>> listener) {
        if (listener == null) {
            return false;
        }

        categories.removeListener(listener);
        return true;
    }

    public boolean addCompanyNamesMapListener(MapChangeListener<String, String> listener) {
        if (listener == null) {
            return false;
        }

        companyNames.addListener(listener);
        return true;
    }

    public boolean removeCompanyNamesMapListener(MapChangeListener<String, String> listener) {
        if (listener == null) {
            return false;
        }

        companyNames.removeListener(listener);
        return true;
    }

    public Collection<String> getUserDefinedCategoryNames() {
        return categories.keySet();
    }

    public Collection<String> getTopMostCategories() {
        return Collections.unmodifiableCollection(topMostCategories);
    }

    public Collection<String> getCompanyIdentifierStrings() {
        return companyNames.keySet();
    }

    public Collection<String> getCategory(final String categoryName) {
        Objects.requireNonNull(categoryName);
        return categories.get(categoryName);
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
    
    public String getCompanyIdentifierString(final String companyDisplayName){
        return companyNamesReversed.get(companyDisplayName);
    }

    public boolean addDefinition(final String categoryName, Collection<String> subCategories) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(subCategories);
        if (categories.containsKey(categoryName)) {
            return false;
        }

        categories.put(categoryName, subCategories);
        if (!topMostCategories.contains(categoryName)) {
            topMostCategories.add(categoryName);
        }
        topMostCategories.removeAll(subCategories);
        return true;
    }

    public boolean removeDefinition(final String categoryName) {
        Objects.requireNonNull(categoryName);
        if (!categories.containsKey(categoryName)) {
            return false;
        }
        Collection<String> subCategories = categories.remove(categoryName);
        topMostCategories.remove(categoryName);
        topMostCategories.addAll(subCategories);
        return true;
    }

    public boolean updateDefinition(final String categoryName, final Collection<String> subCategories) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(subCategories);
        if (!categories.containsKey(categoryName)) {
            return false;
        }
        categories.get(categoryName).addAll(subCategories);
        topMostCategories.removeAll(subCategories);
        return true;
    }

    public String getCSVInputFolder() {
        return csvInputFolder;
    }

    public void setCSVInputFolder(final String csvInputFolder) {
        this.csvInputFolder = csvInputFolder;
    }

    public Timeframe getTransactionGroupingTimeframe(){
        return transactionsTimeframe;
    }
    
    public void setTransactionGroupingTimeframe(Timeframe timeframe){
        this.transactionsTimeframe = timeframe;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPreferences that = (UserPreferences) o;
        return Objects.equals(categories, that.categories)
                && Objects.equals(csvInputFolder, that.csvInputFolder)
                 && transactionsTimeframe == that.transactionsTimeframe
                && Objects.equals(companyNames, that.companyNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categories, csvInputFolder, transactionsTimeframe, companyNames);
    }
}
