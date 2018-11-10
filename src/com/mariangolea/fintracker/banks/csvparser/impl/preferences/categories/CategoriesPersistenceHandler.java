package com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandlerBase;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CategoriesPersistenceHandler extends UserPreferencesHandlerBase {

    public static final String CATEGORIES_FILE_DEFAULT_NAME = "categories.properties";

    private static final String CATEGORY_NAMES = "categories";
    private static final String SEPARATOR = ";";

    private final Properties categoriesFile = new Properties();

    public CategoriesPersistenceHandler(final UserPreferences userPreferences) {
        super(userPreferences);
    }

    public CategoriesPersistenceHandler(final UserPreferences userPreferences, final String preferencesFolder) {
        super(userPreferences, preferencesFolder);
    }

    @Override
    public boolean storePreferences() {
        return storeCategoriesFile();
    }

    @Override
    public void loadPreferences() {
        loadCategoryNamesFile();
    }

    protected void loadCategoryNamesFile() {
        categoriesFile.putAll(loadProperties(CATEGORIES_FILE_DEFAULT_NAME));
        String categoryNamesString = categoriesFile.getProperty(CATEGORY_NAMES);
        Set<String> categoryNames = new HashSet<>(
                convertPersistedStringToList(categoryNamesString, SEPARATOR));
        userPreferences.appendDefinition(CategoriesTree.ROOT, categoryNames);
        categoryNames.forEach((topMostCategory) -> {
            loadCategoryName(topMostCategory);
        });
    }

    protected boolean storeCategoriesFile() {
        final Collection<String> categoryNames = userPreferences.getTopMostCategories();
        String categoryNamesValue = convertStringsForStorage(categoryNames, SEPARATOR);
        if (categoryNamesValue != null && !categoryNamesValue.isEmpty()) {
            categoriesFile.setProperty(CATEGORY_NAMES, categoryNamesValue);
            categoryNames.forEach((categoryName) -> {
                storeTopMostCategoryName(categoryName);
            });
        }

        return storeProperties(CATEGORIES_FILE_DEFAULT_NAME, categoriesFile, COMMENTS);
    }

    private void storeTopMostCategoryName(final String topMostCategory) {
        Collection<String> subCategories = userPreferences.getSubCategories(topMostCategory);
        if (subCategories != null && !subCategories.isEmpty()) {
            categoriesFile.setProperty(topMostCategory, convertStringsForStorage(subCategories, SEPARATOR));
            subCategories.forEach((category) -> {
                storeTopMostCategoryName(category);
            });
        }
    }

    private void loadCategoryName(final String category) {
        String categoryNamesString = categoriesFile.getProperty(category);
        Set<String> subCategories = new HashSet<>(
                convertPersistedStringToList(categoryNamesString, SEPARATOR));
        if (subCategories.isEmpty()) {
            return;
        }
        userPreferences.appendDefinition(category, subCategories);
        subCategories.forEach((subCategoy) -> {
            loadCategoryName(subCategoy);
        });
    }

}
