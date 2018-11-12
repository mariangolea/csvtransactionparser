package com.mariangolea.fintracker.banks.csvparser.impl.preferences.companynames;

import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandlerBase;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import javafx.collections.FXCollections;

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
        final Map<String, Collection<String>> companyNamesMap = FXCollections.observableHashMap();
        companyNamesFile.keySet().forEach(companyIdentifier -> {
            final String companyDisplayName = companyNamesFile.getProperty(companyIdentifier.toString());
            Collection<String> existingIdentifiers = companyNamesMap.get(companyDisplayName);
            if (existingIdentifiers == null){
                existingIdentifiers = FXCollections.observableArrayList();
                companyNamesMap.put(companyDisplayName, existingIdentifiers);
            }
            existingIdentifiers.add(companyIdentifier.toString());
        });
        companyNamesMap.keySet().forEach(companyName -> userPreferences.resetCompanyIdentifierStrings(companyName, companyNamesMap.get(companyName)));
    }

    protected boolean storeCompanyNamesFile() {
        userPreferences.getAllCompanyIdentifierStrings().forEach((companyKey) -> {
            companyNamesFile.setProperty(companyKey, userPreferences.getCompanyDisplayName(companyKey));
        });

        return storeProperties(COMPANY_NAMES_FILE_DEFAULT_NAME, companyNamesFile, COMMENTS);
    }

}
