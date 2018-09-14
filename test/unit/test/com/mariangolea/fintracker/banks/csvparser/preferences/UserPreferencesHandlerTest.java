/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserDefinedTransactionGroup;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;

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
        assertTrue(prefs.getCSVInputFolder() == null);
    }

    @Test
    public void testBehaviorStorePreferences() {
        handler.deletePreferencesFile();
        UserPreferences prefs = handler.loadUserPreferences();
        prefs.setCSVInputFolder("useless");
        UserDefinedTransactionGroup group = new UserDefinedTransactionGroup("category1");
        group.addAssociations("first", new HashSet<>(Arrays.asList("1", "2")));
        group.addAssociations("swift", new HashSet<>(Arrays.asList("1", "2")));
        prefs.addDefinition("category1", group);
        prefs.setTransactionDisplayName("incasare", "incasareDisplayName");
        // after this store, next load should retrieve a different objects with same
        // contents.
        boolean stored = handler.storePreferences(prefs);
        assertTrue(stored);
        UserPreferences loaded = handler.loadUserPreferences();
        assertTrue(prefs.equals(loaded));

        boolean deleted = handler.deletePreferencesFile();
        assertTrue(deleted);
    }
}
