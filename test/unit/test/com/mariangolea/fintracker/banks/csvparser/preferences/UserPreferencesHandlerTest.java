package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandlerFactory;
import java.util.Arrays;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;

public class UserPreferencesHandlerTest {

    @Test
    public void testDefaultHandler(){
        UserPreferencesHandlerFactory original = new UserPreferencesHandlerFactory();
        assertNotNull(original.getUserPreferencesHandler());
    }
    
    @Test
    public void testFirstLoadPreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesHandlerInterface handler = factory.getUserPreferencesHandler();
        // delete preferences file to verify initial app behavior.
        UserPreferencesInterface prefs = handler.getPreferences();
        assertNotNull(prefs.getUserDefinedCategoryNames());
        assertTrue(prefs.getUserDefinedCategoryNames().isEmpty());
        assertNull(prefs.getCSVInputFolder());
    }

    @Test
    public void testBehaviorStorePreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesHandlerInterface handler = factory.getUserPreferencesHandler();
        UserPreferencesInterface prefs = handler.getPreferences();
        prefs.setCSVInputFolder("useless");
        prefs.appendDefinition("category1", Utilities.createList("1", "2"));
        prefs.resetCompanyIdentifierStrings("incasareDisplayName", Arrays.asList("incasare"));
        // after this store, next load should retrieve a different objects with same
        // contents.
        boolean stored = handler.storePreferences();
        assertTrue(stored);
        UserPreferencesInterface loaded = handler.getPreferences();
        assertTrue(prefs.equals(loaded));
    }
}
