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
 *
 * @author mariangolea@gmail.com
 */
public class UserPreferencesHandler {

    private static final String COMMENTS = "Automatically generated. DO NOT EDIT YOURSELF!!!";
    private static final String USER_PREFERENCES_FILE_DEFAULT_NAME = "userprefs.properties";
    private static final String USER_PREFERENCES_FILE_PATH_DEFAULT = "./" + USER_PREFERENCES_FILE_DEFAULT_NAME;
    private static final String COMPANY_NAMES_FILE_DEFAULT_NAME = "companynames.properties";
    private static final String COMPANY_NAMES_FILE_PATH_DEFAULT = "./" + COMPANY_NAMES_FILE_DEFAULT_NAME;

    private static final String CATEGORY_NAMES = "categories";
    private static final String INPUT_FOLDER = "inputFolder";
    private static final String SEPARATOR_BANKS = ";";
    private static final String SEPARATOR_TRANSACTIONS = ":";
    private static final String SEPARATOR_USER_CATEGORIES = ",";

    private final Properties userPrefsFile = new Properties();
    private final Properties companyNamesFile = new Properties();
    private UserPreferences userPreferences;
    
    private static class Holder{
        private static UserPreferencesHandler HANDLER = new UserPreferencesHandler();
    }
    
    private UserPreferencesHandler(){}
    
    public static UserPreferencesHandler getInstance(){
        return Holder.HANDLER;
    }
    
    public UserPreferences getPreferences(){
        if (userPreferences == null){
            userPreferences = loadUserPreferences();
        }
        return userPreferences;
    }
    
    /**
     * Get the user preferences.
     *
     * @return user preferences
     */
    private UserPreferences loadUserPreferences() {
        UserPreferences loadedPrefs = new UserPreferences();
        loadUserPrefsFile(loadedPrefs);
        loadCompanyNamesFile(loadedPrefs);

        return loadedPrefs;
    }

    /**
     * Stores user preferences. Depending on user choice, this happens on a default
     * location or a user defined one.
     *
     * @return true if successfull
     */
    public boolean storePreferences() {
        boolean success = storeUserPrefsFile(userPreferences);
        success &= storeCompanyNamesFile(userPreferences);
        return success;
    }

    public boolean deletePreferencesFile() {
        File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
        if (propertiesFile.exists()) {
            return propertiesFile.delete();
        }
        return true;
    }

    private void loadUserPrefsFile(final UserPreferences userPreferences) {
        File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
        try {
            if (propertiesFile.exists()) {
                FileReader reader = new FileReader(propertiesFile);
                userPrefsFile.load(reader);
                reader.close();
                userPreferences.setCSVInputFolder(userPrefsFile.getProperty(INPUT_FOLDER));
                String categoryNamesString = userPrefsFile.getProperty(CATEGORY_NAMES);
                Set<String> categoryNames = new HashSet<String>(
                        convertPersistedStringToList(categoryNamesString, SEPARATOR_USER_CATEGORIES));
                for (String categoryName : categoryNames) {
                    Set<String> bankGroups = new HashSet<String>(
                            convertPersistedStringToList(userPrefsFile.getProperty(categoryName), SEPARATOR_BANKS));
                    UserDefinedTransactionGroup userGroup = new UserDefinedTransactionGroup(categoryName);
                    for (String bankGroup : bankGroups) {
                        List<String> bankWithTransactions = convertPersistedStringToList(bankGroup,
                                SEPARATOR_TRANSACTIONS);
                        String swiftCode = bankWithTransactions.toArray()[0].toString();
                        Set<String> transactions = new HashSet<String>(convertPersistedStringToList(
                                bankWithTransactions.toArray()[1].toString(), SEPARATOR_USER_CATEGORIES));
                        userGroup.addAssociations(swiftCode, transactions);
                    }
                    userPreferences.addDefinition(categoryName, userGroup);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCompanyNamesFile(final UserPreferences userPreferences) {
        File propertiesFile = new File(COMPANY_NAMES_FILE_PATH_DEFAULT);
        try {
            if (propertiesFile.exists()) {
                FileReader reader = new FileReader(propertiesFile);
                companyNamesFile.load(reader);
                reader.close();
                for (Object categoryName : companyNamesFile.keySet()) {
                    userPreferences.setTransactionDisplayName(categoryName.toString(), companyNamesFile.get(categoryName).toString());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private boolean storeUserPrefsFile(final UserPreferences userPreferences) {
        userPrefsFile.setProperty(INPUT_FOLDER, userPreferences.getCSVInputFolder());
        final Set<String> categoryNames = userPreferences.getUserDefinedCategoryNames();
        String categoryNamesValue = convertStringsForStorage(categoryNames, SEPARATOR_USER_CATEGORIES);
        if (categoryNamesValue != null && !categoryNamesValue.isEmpty()) {
            userPrefsFile.setProperty(CATEGORY_NAMES, categoryNamesValue);
            for (String categoryName : categoryNames) {
                UserDefinedTransactionGroup userGroup = userPreferences.getDefinition(categoryName);
                Set<String> swiftCodes = userGroup.getSwiftCodes();
                Set<String> perBank = new HashSet<>();
                for (String swiftCode : swiftCodes) {
                    String forSwiftCode = convertStringsForStorage(userGroup.getTransactionsFor(swiftCode),
                            SEPARATOR_USER_CATEGORIES);
                    perBank.add(swiftCode + SEPARATOR_TRANSACTIONS + forSwiftCode);
                }
                userPrefsFile.setProperty(categoryName, convertStringsForStorage(perBank, SEPARATOR_BANKS));
            }
        }

        File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
        AtomicBoolean success = new AtomicBoolean(false);
        try {
            if (!propertiesFile.exists()) {
                if (!propertiesFile.createNewFile()) {
                    return false;
                }
            }
            FileWriter writer = new FileWriter(propertiesFile);
            userPrefsFile.store(writer, COMMENTS);
            writer.close();
            success.set(true);

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return success.get();
    }

    private boolean storeCompanyNamesFile(final UserPreferences userPreferences) {
        for (String transactionDescString : userPreferences.getUserDefinedCompanyNames()) {
            companyNamesFile.setProperty(transactionDescString, userPreferences.getDisplayName(transactionDescString));
        }

        File propertiesFile = new File(COMPANY_NAMES_FILE_PATH_DEFAULT);
        AtomicBoolean success = new AtomicBoolean(false);
        try {
            if (!propertiesFile.exists()) {
                if (!propertiesFile.createNewFile()) {
                    return false;
                }
            }
            FileWriter writer = new FileWriter(propertiesFile);
            companyNamesFile.store(writer, COMMENTS);
            writer.close();
            success.set(true);

        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return success.get();
    }


    private String convertStringsForStorage(final Set<String> strings, final String separator) {
        String soleString = "";
        if (strings == null || strings.isEmpty()) {
            return soleString;
        }
        for (String categoryName : strings) {
            soleString += categoryName + separator;
        }
        soleString = soleString.substring(0, soleString.length() - 1);

        return soleString;
    }

    private List<String> convertPersistedStringToList(final String string, final String separator) {
        List<String> strings = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return strings;
        }
        String[] split = string.split(separator);
        strings.addAll(Arrays.asList(split));
        return strings;
    }
}
