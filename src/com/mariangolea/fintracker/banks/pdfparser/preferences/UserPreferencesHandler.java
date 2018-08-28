package com.mariangolea.fintracker.banks.pdfparser.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores user preferences: 1. grouping of certain transactions. allows
 * application to automatically group transactions in user defined categories.
 * 2. file containing this information.
 *
 * <br> To allow flexibility, the only fixed property name is the one whose
 * values are all category names.
 * <br> After parsing all category names, each category string will also be a
 * key in the same file, where all similar transaction names are to be found.
 *
 * @author mariangolea@gmail.com
 */
public class UserPreferencesHandler {

    private static final String COMMENTS = "Automatically generated. DO NOT EDIT YOURSELF!!!";
    private static final String USER_PREFERENCES_FILE_DEFAULT_NAME = "userprefs.properties";
    private static final String USER_PREFERENCES_FILE_PATH_DEFAULT = "./" + USER_PREFERENCES_FILE_DEFAULT_NAME;
    private static final String CATEGORY_NAMES = "categories";
    private static final String INPUT_FOLDER = "inputFolder";

    private final Properties userPrefsFile = new Properties();

    /**
     * Starts up null. If user does not set it, defaults will be used the first
     * time a store is needed.
     */
    private String userPreferencesFilePath;

    /**
     * Get the user preferences.
     *
     * @return user preferences
     */
    public UserPreferences loadUserPreferences() {
        UserPreferences loadedPrefs = new UserPreferences();
        File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
        try {
            if (propertiesFile.exists()) {
                userPrefsFile.load(new FileReader(propertiesFile));
                loadedPrefs.setPDFInputFolder(userPrefsFile.getProperty(INPUT_FOLDER));
                String categoryNamesString = userPrefsFile.getProperty(CATEGORY_NAMES);
                List<String> categoryNames = convertPersistedStringToList(categoryNamesString);
                for (String categoryName : categoryNames) {
                    List<String> subCategories = convertPersistedStringToList(userPrefsFile.getProperty(categoryName));
                    loadedPrefs.addUpdateCategory(categoryName, subCategories);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return loadedPrefs;
    }

    /**
     * Stores user preferences. Depending on user choice, this happens on a
     * default location or a user defined one.
     *
     * @param userPreferences
     * @return true if successfull
     */
    public boolean storePreferences(final UserPreferences userPreferences) {
        boolean success = true;
        userPrefsFile.setProperty(INPUT_FOLDER, userPreferences.getPDFInputFolder());
        final Map<String, List<String>> categoryAssociations = userPreferences.getAllCategories();
        final List<String> categoryNames = new ArrayList<>();
        String categoryNamesValue = convertStringsForStorage(categoryAssociations.keySet());
        if (categoryNamesValue != null && categoryNamesValue.isEmpty()) {
            userPrefsFile.setProperty(CATEGORY_NAMES, categoryNamesValue);
            for (String categoryName : categoryAssociations.keySet()) {
                userPrefsFile.setProperty(categoryName, convertStringsForStorage(categoryAssociations.get(categoryNamesValue)));
            }
        }

        File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
        try {
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }
            userPrefsFile.store(new FileWriter(propertiesFile), COMMENTS);

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }

        return success;
    }

    private String convertStringsForStorage(final Collection<String> strings) {
        String soleString = "";
        if (strings == null || strings.isEmpty()) {
            return soleString;
        }
        for (String categoryName : strings) {
            soleString += categoryName + ",";
        }
        soleString = soleString.substring(0, soleString.length() - 1);

        return soleString;
    }

    private List<String> convertPersistedStringToList(final String string) {
        List<String> strings = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return strings;
        }
        String[] split = string.split(",");
        strings.addAll(Arrays.asList(split));
        return strings;
    }
}
