package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class UserPreferencesHandlerBase {

    protected final static String COMMENTS = "Automatically generated. DO NOT EDIT YOURSELF!!!";
    protected static final String SUB_FOLDER = "preferences";

    protected final UserPreferences userPreferences;
    private final String prefsFolder;

    public UserPreferencesHandlerBase(final UserPreferences userPreferences) {
        this(userPreferences, SUB_FOLDER);
    }

    public UserPreferencesHandlerBase(final UserPreferences userPreferences, final String preferencesFolder) {
        this.userPreferences = Objects.requireNonNull(userPreferences);
        prefsFolder = Objects.requireNonNull(preferencesFolder);
        File folder = new File(prefsFolder);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
    }

    public abstract void loadPreferences();

    public abstract boolean storePreferences();
    
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

    protected boolean storeProperties(final String filePath, final Properties properties, final String comments) {
        File propertiesFile = new File(prefsFolder, filePath);
        try {
            if (!propertiesFile.exists() && !propertiesFile.createNewFile()) {
                return false;
            }
            try (FileWriter writer = new FileWriter(propertiesFile)) {
                properties.store(writer, comments);
            }
            return true;

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    protected Properties loadProperties(final String filePath) {
        File propertiesFile = new File(prefsFolder, filePath);
        Properties properties = new Properties();
        try {
            if (!propertiesFile.exists()) {
                if (!propertiesFile.createNewFile()) {
                    return properties;
                }
            }
            try (FileReader reader = new FileReader(propertiesFile)) {
                properties.load(reader);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return properties;
    }

}
