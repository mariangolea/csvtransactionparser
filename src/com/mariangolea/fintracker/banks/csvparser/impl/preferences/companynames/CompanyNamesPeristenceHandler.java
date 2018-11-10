package com.mariangolea.fintracker.banks.csvparser.impl.preferences.companynames;

import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandlerBase;
import java.util.Properties;

public class CompanyNamesPeristenceHandler extends UserPreferencesHandlerBase {

    public static final String COMPANY_NAMES_FILE_DEFAULT_NAME = "companynames.properties";
    private final Properties companyNamesFile = new Properties();

    public CompanyNamesPeristenceHandler(final UserPreferences userPreferences) {
        super(userPreferences);
    }

    public CompanyNamesPeristenceHandler(final UserPreferences userPreferences, final String preferencesFolder) {
        super(userPreferences, preferencesFolder);
    }

    @Override
    public boolean storePreferences() {
        return storeCompanyNamesFile();
    }

    @Override
    public void loadPreferences() {
        loadCompanyNamesFile();
    }

    protected void loadCompanyNamesFile() {
        companyNamesFile.putAll(loadProperties(COMPANY_NAMES_FILE_DEFAULT_NAME));
        companyNamesFile.keySet().forEach(categoryName -> {
            userPreferences.setCompanyDisplayName(categoryName.toString(), companyNamesFile.get(categoryName).toString());
        });
    }

    protected boolean storeCompanyNamesFile() {
        userPreferences.getAllCompanyIdentifierStrings().forEach((companyKey) -> {
            companyNamesFile.setProperty(companyKey, userPreferences.getCompanyDisplayName(companyKey));
        });

        return storeProperties(COMPANY_NAMES_FILE_DEFAULT_NAME, companyNamesFile, COMMENTS);
    }

}
