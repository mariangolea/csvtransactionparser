package com.mariangolea.fintracker.banks.pdfparser.preferences;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.UserDefinedTransactionGroup;

/**
 * Container of user preferences.
 *
 * @author mariangolea@gmail.com
 */
public class UserPreferences {

	private final Map<String, UserDefinedTransactionGroup> transactionCategories = new HashMap<>();
	private String pdfInputFolder;

	/**
	 * Get a ordered set of available user defined categories.
	 * 
	 * @return user defined categories
	 */
	public Set<String> getUserDefinedCategoryNames() {
		return transactionCategories.keySet();
	}

	public UserDefinedTransactionGroup getDefinition(final String categoryName) {
		return transactionCategories.get(categoryName);
	}

	public boolean addDefinition(final String categoryName, UserDefinedTransactionGroup newDefinition) {
		if (transactionCategories.containsKey(categoryName)) {
			return false;
		}

		transactionCategories.put(categoryName, newDefinition);
		return true;
	}

	public boolean removeDefinition(final String categoryName) {
		if (!transactionCategories.containsKey(categoryName)) {
			return false;
		}
		transactionCategories.remove(categoryName);
		return true;
	}

	public boolean updateDefinition(final String categoryName, UserDefinedTransactionGroup newDefinition) {
		if (!transactionCategories.containsKey(categoryName)) {
			return false;
		}
		transactionCategories.put(categoryName, newDefinition);
		return true;
	}

	public Collection<UserDefinedTransactionGroup> getUserDefinitions() {
		return transactionCategories.values();
	}

	public String getPDFInputFolder() {
		return pdfInputFolder;
	}

	public void setPDFInputFolder(final String pdfInputFolder) {
		this.pdfInputFolder = pdfInputFolder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pdfInputFolder == null) ? 0 : pdfInputFolder.hashCode());
		result = prime * result + ((transactionCategories == null) ? 0 : transactionCategories.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPreferences other = (UserPreferences) obj;
		if (pdfInputFolder == null) {
			if (other.pdfInputFolder != null)
				return false;
		} else if (!pdfInputFolder.equals(other.pdfInputFolder))
			return false;
		if (transactionCategories == null) {
			if (other.transactionCategories != null)
				return false;
		} else if (!transactionCategories.equals(other.transactionCategories))
			return false;
		return true;
	}

}
