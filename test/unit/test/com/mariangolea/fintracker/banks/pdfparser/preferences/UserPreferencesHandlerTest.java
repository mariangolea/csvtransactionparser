/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.preferences;

import com.mariangolea.fintracker.banks.pdfparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.pdfparser.preferences.UserPreferencesHandler;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class UserPreferencesHandlerTest {

    private final UserPreferencesHandler handler = new UserPreferencesHandler();

    @Test
    public void testFirstLoadPreferences() {
        //delete preferences file to verify initial app behavior.
        boolean existed = handler.deletePreferencesFile();
        UserPreferences prefs = handler.loadUserPreferences();
        assertTrue(prefs.getAllCategories() != null && prefs.getAllCategories().isEmpty());
        assertTrue(prefs.getPDFInputFolder() == null);
    }

    @Test
    public void testBehaviorStorePreferences() {
        UserPreferences prefs = handler.loadUserPreferences();
        prefs.setPDFInputFolder("useless");
        prefs.addUpdateCategory("1", Arrays.asList("one", "first"));
        prefs.addUpdateCategory("2", Arrays.asList("two", "second"));

        //after this store, next load should retrieve a difference objects with same contents.
        boolean stored = handler.storePreferences(prefs);
        assertTrue(stored);
        UserPreferences loaded = handler.loadUserPreferences();
        assertTrue(prefs.equals(loaded));
        
        prefs.addUpdateCategory("1", Arrays.asList("third"));
        assertTrue(!prefs.equals(loaded));
    }
}
