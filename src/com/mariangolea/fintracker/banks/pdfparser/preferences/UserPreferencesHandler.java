package com.mariangolea.fintracker.banks.pdfparser.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.UserDefinedTransactionGroup;

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
	private static final String CATEGORY_NAMES = "categories";
	private static final String INPUT_FOLDER = "inputFolder";
	private static final String SEPARATOR_BANKS = ";";
	private static final String SEPARATOR_TRANSACTIONS = ":";
	private static final String SEPARATOR_USER_CATEGORIES = ",";

	private final Properties userPrefsFile = new Properties();

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
				FileReader reader = new FileReader(propertiesFile);
				userPrefsFile.load(reader);
				reader.close();
				loadedPrefs.setPDFInputFolder(userPrefsFile.getProperty(INPUT_FOLDER));
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
					loadedPrefs.addDefinition(categoryName, userGroup);
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return loadedPrefs;
	}

	/**
	 * Stores user preferences. Depending on user choice, this happens on a default
	 * location or a user defined one.
	 *
	 * @param userPreferences
	 * @return true if successfull
	 */
	public boolean storePreferences(final UserPreferences userPreferences) {
		boolean success = false;
		userPrefsFile.setProperty(INPUT_FOLDER, userPreferences.getPDFInputFolder());
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
		try {
			if (!propertiesFile.exists()) {
				if (!propertiesFile.createNewFile()) {
					return false;
				}
			}
			FileWriter writer = new FileWriter(propertiesFile);
			userPrefsFile.store(writer, COMMENTS);
			writer.close();
			success = true;

		} catch (IOException ex) {
			Logger.getLogger(UserPreferencesHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return success;
	}

	public boolean deletePreferencesFile() {
		File propertiesFile = new File(USER_PREFERENCES_FILE_PATH_DEFAULT);
		if (propertiesFile.exists()) {
			return propertiesFile.delete();
		}
		return true;
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
