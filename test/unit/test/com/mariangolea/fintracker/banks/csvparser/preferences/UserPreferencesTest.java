package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.Arrays;

public class UserPreferencesTest {

    @Test
    public void testMethods() {
        UserPreferences prefs = new UserPreferences();
        boolean success = prefs.addDefinition("one", Arrays.asList("one"));
        assertTrue(!success);

        success = prefs.removeDefinition("fake");
        assertTrue(!success);

        success = prefs.removeDefinition("one");
        assertTrue(success);

        success = prefs.updateDefinition("one", Arrays.asList("one"));
        assertTrue(!success);

        success = prefs.updateDefinition("one", Arrays.asList("one"));
        assertTrue(success);

        UserPreferences other = new UserPreferences();
        other.addDefinition("one", Arrays.asList("one"));
        assertTrue(prefs.equals(other));
    }
}
