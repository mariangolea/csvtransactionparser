package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class UserPreferencesHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testFirstLoadPreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory(TestUtilities.createFolder(folder, "prefsTest"));
        UserPreferencesHandlerInterface handler = factory.getUserPreferencesHandler();
        // delete preferences file to verify initial app behavior.
        UserPreferencesInterface prefs = handler.getPreferences();
        assertNotNull(prefs.getUserDefinedCategoryNames());
        assertTrue(prefs.getUserDefinedCategoryNames().isEmpty());
        assertNull(prefs.getCSVInputFolder());
    }

    @Test
    public void testBehaviorStorePreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory(TestUtilities.createFolder(folder, "prefsTest"));
        UserPreferencesHandlerInterface handler = factory.getUserPreferencesHandler();
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
    }
}
