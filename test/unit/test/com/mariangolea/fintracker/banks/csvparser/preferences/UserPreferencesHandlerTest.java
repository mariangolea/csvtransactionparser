package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserPreferencesHandlerTest {

    private final UserPreferencesHandler handler = UserPreferencesHandler.INSTANCE;

    @Test
    public void testFirstLoadPreferences() {
        // delete preferences file to verify initial app behavior.
        handler.deletePreferences();
        UserPreferences prefs = handler.getPreferences();
        assertNotNull(prefs.getUserDefinedCategoryNames());
        assertTrue(prefs.getUserDefinedCategoryNames().isEmpty());
        assertNull(prefs.getCSVInputFolder());
    }

    @Test
    public void testBehaviorStorePreferences() {
        handler.deletePreferences();
        UserPreferences prefs = handler.getPreferences();
        prefs.setCSVInputFolder("useless");
        prefs.setDefinition("category1", Arrays.asList("1", "2"));
        prefs.setCompanyDisplayName("incasare", "incasareDisplayName");
        // after this store, next load should retrieve a different objects with same
        // contents.
        boolean stored = handler.storePreferences();
        assertTrue(stored);
        UserPreferences loaded = handler.getPreferences();
        assertTrue(prefs.equals(loaded));

        boolean deleted = handler.deletePreferences();
        assertTrue(deleted);
    }
}
