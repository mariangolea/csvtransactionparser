package com.mariangolea.fintracker.banks.csvparser.api.preferences;

import java.util.Collection;

public interface CategoriesInterface extends CompanyNamesInterface{

    public static final String UNCATEGORIZED = "Uncategorized";

    public Collection<String> getUserDefinedCategoryNames();

    public Collection<String> getTopMostCategories();

    public Collection<String> getSubCategories(final String categoryName);

    public String getMatchingCategory(String companyDescriptionString);

    public void appendDefinition(final String categoryName, final Collection<String> subCategories);

    public String getParent(final String categoryName);
}
