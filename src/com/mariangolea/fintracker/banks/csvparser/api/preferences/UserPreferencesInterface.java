package com.mariangolea.fintracker.banks.csvparser.api.preferences;

import java.util.Calendar;
import java.util.Collection;

public interface UserPreferencesInterface {

    public static final String UNCATEGORIZED = "Uncategorized";

    public enum Timeframe {
        MONTH(Calendar.MONTH),
        YEAR(Calendar.YEAR);

        public final int timeframe;

        private Timeframe(int timeframe) {
            this.timeframe = timeframe;
        }
    }
    
    public Collection<String> getUserDefinedCategoryNames();

    public Collection<String> getTopMostCategories();

    public Collection<String> getCompanyIdentifierStrings();

    public Collection<String> getSubCategories(final String categoryName);

    public void setCompanyDisplayName(final String company, final String displayName);

    public String getCompanyDisplayName(final String company);

    public Collection<String> getCompanyDisplayNames();

    public String getMatchingCategory(String companyDescriptionString);

    public String getCompanyIdentifierString(final String companyDisplayName);

    public void appendDefinition(final String categoryName, final Collection<String> subCategories);

    public String getParent(final String categoryName);

    public String getCSVInputFolder();

    public void setCSVInputFolder(final String csvInputFolder);

    public Timeframe getTransactionGroupingTimeframe();

    public void setTransactionGroupingTimeframe(Timeframe timeframe);
}
