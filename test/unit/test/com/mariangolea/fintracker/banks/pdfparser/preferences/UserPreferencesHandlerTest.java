/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.preferences;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.UserDefinedTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.pdfparser.preferences.UserPreferencesHandler;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class UserPreferencesHandlerTest {

	private final UserPreferencesHandler handler = new UserPreferencesHandler();

	@Test
	public void testFirstLoadPreferences() {
		// delete preferences file to verify initial app behavior.
		handler.deletePreferencesFile();
		UserPreferences prefs = handler.loadUserPreferences();
		assertTrue(prefs.getUserDefinitions() != null && prefs.getUserDefinitions().isEmpty());
		assertTrue(prefs.getPDFInputFolder() == null);
	}

	@Test
	public void testBehaviorStorePreferences() {
		UserPreferences prefs = handler.loadUserPreferences();
		prefs.setPDFInputFolder("useless");
		UserDefinedTransactionGroup group = new UserDefinedTransactionGroup("useless");
		group.addAssociations(Bank.ING.swiftCode, new HashSet<>(Arrays.asList("1", "2")));
		group.addAssociations("swift", new HashSet<>(Arrays.asList("1", "2")));

		// after this store, next load should retrieve a different objects with same
		// contents.
		boolean stored = handler.storePreferences(prefs);
		assertTrue(stored);
		UserPreferences loaded = handler.loadUserPreferences();
		assertTrue(prefs.equals(loaded));
	}
}
