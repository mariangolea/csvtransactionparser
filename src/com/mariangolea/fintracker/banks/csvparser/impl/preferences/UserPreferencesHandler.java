package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesPersistenceHandler;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.companynames.CompanyNamesPeristenceHandler;
import java.util.Properties;

public class UserPreferencesHandler extends UserPreferencesHandlerBase implements UserPreferencesHandlerInterface {

    public static final String USER_PREFERENCES_FILE_DEFAULT_NAME = "userprefs.properties";

    private static final String INPUT_FOLDER = "inputFolder";
    private static final String TRANSACTION_GROUPING_TIMEFRAME = "timeFrame";

    private final Properties userPrefsFile = new Properties();
    private final CategoriesPersistenceHandler categoriesHandler;
    private final CompanyNamesPeristenceHandler companiesHandler;

    public UserPreferencesHandler() {
        super(new UserPreferences());
        categoriesHandler = new CategoriesPersistenceHandler(userPreferences);
        companiesHandler = new CompanyNamesPeristenceHandler(userPreferences);
        loadPreferences();
    }

    public UserPreferencesHandler(final String preferencesFolder) {
        super(new UserPreferences(), preferencesFolder);
        categoriesHandler = new CategoriesPersistenceHandler(userPreferences, preferencesFolder);
        companiesHandler = new CompanyNamesPeristenceHandler(userPreferences, preferencesFolder);
        loadPreferences();
    }

    @Override
    public boolean storePreferences() {
        boolean success = storeUserPrefsFile();
        companiesHandler.storePreferences();
        categoriesHandler.storePreferences();
        return success;
    }

    @Override
    public final void loadPreferences() {
        loadUserPrefsFile();
        //company names must be read before categories.
        companiesHandler.loadPreferences();
        categoriesHandler.loadPreferences();
    }

    protected void loadUserPrefsFile() {
        userPrefsFile.putAll(loadProperties(USER_PREFERENCES_FILE_DEFAULT_NAME));
        userPreferences.setCSVInputFolder(userPrefsFile.getProperty(INPUT_FOLDER));
        UserPreferencesInterface.Timeframe timeFrame = UserPreferencesInterface.Timeframe.valueOf(userPrefsFile.getProperty(TRANSACTION_GROUPING_TIMEFRAME, UserPreferencesInterface.Timeframe.MONTH.name()));
        userPreferences.setTransactionGroupingTimeframe(timeFrame);
    }

    protected boolean storeUserPrefsFile() {
        if (userPreferences.getCSVInputFolder() != null) {
            userPrefsFile.setProperty(INPUT_FOLDER, userPreferences.getCSVInputFolder());
        }
        userPrefsFile.setProperty(TRANSACTION_GROUPING_TIMEFRAME, userPreferences.getTransactionGroupingTimeframe().name());
        return storeProperties(USER_PREFERENCES_FILE_DEFAULT_NAME, userPrefsFile, COMMENTS);
    }

    @Override
    public UserPreferencesInterface getPreferences() {
        return userPreferences;
    }

    @Override
    public UserPreferencesInterface deepCopyPreferences(UserPreferencesInterface original) {
        return new UserPreferences(userPreferences);
    }
}
