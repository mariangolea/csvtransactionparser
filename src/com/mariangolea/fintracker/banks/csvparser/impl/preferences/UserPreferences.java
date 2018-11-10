package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface.Timeframe;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesPreferences;
import java.util.Objects;

public class UserPreferences extends CategoriesPreferences implements UserPreferencesInterface {

    private String csvInputFolder;
    private Timeframe transactionsTimeframe = Timeframe.MONTH;

    protected UserPreferences() {
    }

    protected UserPreferences(final CategoriesPreferences prefs) {
        super(Objects.requireNonNull(prefs));
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

    @Override
    public UserPreferences clone() {
        UserPreferences prefs = new UserPreferences(super.clone());
        prefs.csvInputFolder = csvInputFolder;
        prefs.transactionsTimeframe = transactionsTimeframe;
        return prefs;
    }

    @Override
    public void applyChanges(final UserPreferencesInterface userEdited) {
        setTransactionGroupingTimeframe(userEdited.getTransactionGroupingTimeframe());
        setCSVInputFolder(userEdited.getCSVInputFolder());
        super.applyChanges(userEdited);
    }
    
    
}
