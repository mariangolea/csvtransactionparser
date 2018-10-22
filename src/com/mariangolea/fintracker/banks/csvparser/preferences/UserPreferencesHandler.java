package com.mariangolea.fintracker.banks.csvparser.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores user preferences in the following format: <br>
 * categories=name1,name2,name3 <br>
 * name1=swiftcode1:transactionid1,transactionid2;swiftcode2:transactionid2,id3,id4
 * <br>
 * name2=swiftcode1:transactionid3;swiftcode2:transactionid4
 */
public enum UserPreferencesHandler {

    INSTANCE;

    private static final String COMMENTS = "Automatically generated. DO NOT EDIT YOURSELF!!!";
    private static final String USER_PREFERENCES_FILE_DEFAULT_NAME = "userprefs.properties";
    private static final String USER_PREFERENCES_FILE_PATH_DEFAULT = "./" + USER_PREFERENCES_FILE_DEFAULT_NAME;
    private static final String COMPANY_NAMES_FILE_DEFAULT_NAME = "companynames.properties";
    private static final String COMPANY_NAMES_FILE_PATH_DEFAULT = "./" + COMPANY_NAMES_FILE_DEFAULT_NAME;
    private static final String CATEGORIES_FILE_DEFAULT_NAME = "companynames.properties";
    private static final String CATEGORIES_FILE_PATH_DEFAULT = "./" + CATEGORIES_FILE_DEFAULT_NAME;

    private static final String CATEGORY_NAMES = "categories";
    private static final String INPUT_FOLDER = "inputFolder";
    private static final String SEPARATOR = ";";

    private final Properties userPrefsFile = new Properties();
    private final Properties companyNamesFile = new Properties();
    private final Properties categoriesFile = new Properties();
    private UserPreferences userPreferences;

    private UserPreferencesHandler() {
    }

    public UserPreferences getPreferences() {
        if (userPreferences == null) {
            userPreferences = new UserPreferences();
            loadPreferences();
        }
        return userPreferences;
    }

    /**
     * Get the user preferences.
     *
     * @return user preferences
     */
    private void loadPreferences() {
        loadUserPrefsFile();
        loadCompanyNamesFile();
        loadCategoryNamesFile();
    }

    /**
     * Stores user preferences. Depending on user choice, this happens on a
     * default location or a user defined one.
     *
     * @return true if successfull
     */
    public boolean storePreferences() {
        boolean success = storeUserPrefsFile();
        success &= storeCompanyNamesFile();
        success &= storeCategoriesFile();
        return success;
    }

    public boolean deletePreferences() {
        boolean success = deletePreferencesFile(USER_PREFERENCES_FILE_PATH_DEFAULT);
        success |= deletePreferencesFile(COMPANY_NAMES_FILE_PATH_DEFAULT);
        success |= deletePreferencesFile(CATEGORIES_FILE_PATH_DEFAULT);
        return success;
    }

    protected boolean deletePreferencesFile(final String filePath) {
        File propertiesFile = new File(filePath);
        if (propertiesFile.exists()) {
            return propertiesFile.delete();
        }
        return true;
    }

    protected void loadUserPrefsFile() {
        userPrefsFile.putAll(loadProperties(USER_PREFERENCES_FILE_PATH_DEFAULT));
        userPreferences.setCSVInputFolder(userPrefsFile.getProperty(INPUT_FOLDER));
    }

    protected void loadCompanyNamesFile() {
        companyNamesFile.putAll(loadProperties(COMPANY_NAMES_FILE_PATH_DEFAULT));
        companyNamesFile.keySet().forEach((categoryName) -> {
            userPreferences.setCompanyDisplayName(categoryName.toString(), companyNamesFile.get(categoryName).toString());
        });
    }

    protected void loadCategoryNamesFile() {
        categoriesFile.putAll(loadProperties(CATEGORIES_FILE_PATH_DEFAULT));
        String categoryNamesString = userPrefsFile.getProperty(CATEGORY_NAMES);
        Set<String> categoryNames = new HashSet<>(
                convertPersistedStringToList(categoryNamesString, SEPARATOR));
        categoryNames.forEach((categoryName) -> {
            Set<String> categories = new HashSet<>(
                    convertPersistedStringToList(userPrefsFile.getProperty(categoryName), SEPARATOR));
            userPreferences.addDefinition(categoryName, categories);
        });
    }

    protected boolean storeUserPrefsFile() {
        userPrefsFile.setProperty(INPUT_FOLDER, userPreferences.getCSVInputFolder());
        return storeProperties(USER_PREFERENCES_FILE_PATH_DEFAULT, userPrefsFile, COMMENTS);
    }

    protected boolean storeCompanyNamesFile() {
        userPreferences.getCompanyIdentifierStrings().forEach((companyKey) -> {
            companyNamesFile.setProperty(companyKey, userPreferences.getCompanyDisplayName(companyKey));
        });

        return storeProperties(COMPANY_NAMES_FILE_PATH_DEFAULT, companyNamesFile, COMMENTS);
    }

    protected boolean storeCategoriesFile() {
        final Collection<String> categoryNames = userPreferences.getTopMostCategories();
        String categoryNamesValue = convertStringsForStorage(categoryNames, SEPARATOR);
        if (categoryNamesValue != null && !categoryNamesValue.isEmpty()) {
            userPrefsFile.setProperty(CATEGORY_NAMES, categoryNamesValue);
            categoryNames.forEach((categoryName) -> {
                storeTopMostCategoryName(categoryName);
            });
        }

        return storeProperties(CATEGORIES_FILE_PATH_DEFAULT, userPrefsFile, COMMENTS);
    }

    private void storeTopMostCategoryName(final String topMostCategory){
        Collection<String> subCategories = userPreferences.getCategory(topMostCategory);
        if (subCategories != null){
            userPrefsFile.setProperty(topMostCategory, convertStringsForStorage(subCategories, SEPARATOR));
            for(String category : subCategories){
                storeTopMostCategoryName(category);
            }
        }
    }
    
    protected boolean storeProperties(final String filePath, final Properties properties, final String comments) {
        File propertiesFile = new File(filePath);
        AtomicBoolean success = new AtomicBoolean(false);
        try {
            if (!propertiesFile.exists()) {
                if (!propertiesFile.createNewFile()) {
                    return false;
                }
            }
            try (FileWriter writer = new FileWriter(propertiesFile)) {
                properties.store(writer, comments);
            }
            success.set(true);

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return success.get();
    }

    protected Properties loadProperties(final String filePath) {
        File propertiesFile = new File(filePath);
        Properties properties = new Properties();
        try {
            if (propertiesFile.exists()) {
                try (FileReader reader = new FileReader(propertiesFile)) {
                    properties.load(reader);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return properties;
    }

    protected String convertStringsForStorage(final Collection<String> strings, final String separator) {
        String soleString = "";
        if (strings == null || strings.isEmpty()) {
            return soleString;
        }
        soleString = strings.stream().map((categoryName) -> categoryName + separator).reduce(soleString, String::concat);
        soleString = soleString.substring(0, soleString.length() - 1);

        return soleString;
    }

    protected List<String> convertPersistedStringToList(final String string, final String separator) {
        List<String> strings = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return strings;
        }
        String[] split = string.split(separator);
        strings.addAll(Arrays.asList(split));
        return strings;
    }
}
