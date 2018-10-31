package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import java.io.File;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class UserPreferencesHandlerTest {

    private UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
    private final UserPreferencesHandlerInterface handler = factory.getUserPreferencesHandler();

    @Test
    public void testFirstLoadPreferences() {
        // delete preferences file to verify initial app behavior.
        TestUtilities.deletePreferences();
        UserPreferencesInterface prefs = handler.getPreferences();
        assertNotNull(prefs.getUserDefinedCategoryNames());
        assertTrue(prefs.getUserDefinedCategoryNames().isEmpty());
        assertNull(prefs.getCSVInputFolder());
    }

    @Test
    public void testBehaviorStorePreferences() {
        TestUtilities.deletePreferences();
        UserPreferencesInterface prefs = handler.getPreferences();
        prefs.setCSVInputFolder("useless");
        prefs.appendDefinition("category1", TestUtilities.createList("1", "2"));
        prefs.setCompanyDisplayName("incasare", "incasareDisplayName");
        // after this store, next load should retrieve a different objects with same
        // contents.
        boolean stored = handler.storePreferences();
        assertTrue(stored);
        UserPreferencesInterface loaded = handler.getPreferences();
        assertTrue(prefs.equals(loaded));

        TestUtilities.deletePreferences();
    }
}
