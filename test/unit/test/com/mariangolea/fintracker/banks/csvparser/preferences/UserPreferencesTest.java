package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.Arrays;
import java.util.Collection;
import javafx.collections.MapChangeListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class UserPreferencesTest {

    @Test
    public void testMethods() {
        UserPreferences prefs = new UserPreferences();
        boolean success = prefs.setDefinition("one", TestUtilities.createList("one"));
        assertTrue(success);
        success = prefs.setDefinition("one", TestUtilities.createList("one"));
        assertTrue(!success);

        success = prefs.removeDefinition("fake");
        assertTrue(!success);

        success = prefs.removeDefinition("one");
        assertTrue(success);

        prefs.setDefinition("one", Arrays.asList("one"));

        UserPreferences other = new UserPreferences();
        other.setDefinition("one", Arrays.asList("one"));
        assertTrue(prefs.equals(other));
        
        other.setCompanyDisplayName("company", "name");
        String expected = other.getCompanyIdentifierString("name");
        assertEquals(expected, "company");
        assertTrue(other.getCompanyIdentifierStrings().containsAll(TestUtilities.createList("company")));
        Collection<String> topMost = other.getTopMostCategories();
        
        other.setDefinition("two", TestUtilities.createList("one"));
        other.setDefinition("three", TestUtilities.createList("two"));
        other.removeDefinition("two");
        
        Collection<String> expectedList = TestUtilities.createList("one","three");
        assertTrue(topMost.size() == expectedList.size() && topMost.containsAll(expectedList));
    }

    @Test
    public void testListeners() {
        UserPreferences prefs = new UserPreferences();

        assertFalse(prefs.addTransactionCategoriesMapListener(null));
        assertFalse(prefs.addCompanyNamesMapListener(null));
        assertFalse(prefs.removeCompanyNamesMapListener(null));
        assertFalse(prefs.removeTransactionCategoriesMapListener(null));

        Listener listener = new Listener();
        CollectionListener collectionListener = new CollectionListener();
        assertTrue(prefs.addTransactionCategoriesMapListener(collectionListener));
        assertTrue(prefs.removeTransactionCategoriesMapListener(collectionListener));
        assertTrue(prefs.addCompanyNamesMapListener(listener));
        assertTrue(prefs.removeCompanyNamesMapListener(listener));
        
        
        prefs.addTransactionCategoriesMapListener(collectionListener);
        prefs.addCompanyNamesMapListener(listener);
        prefs.setDefinition("one", TestUtilities.createList("one"));
        assertTrue(collectionListener.ticks == 1);
        prefs.removeDefinition("one");
        assertTrue(collectionListener.ticks == 2);
        
        prefs.setCompanyDisplayName("company", "name");
        assertTrue(listener.ticks == 1);

    }

    private class Listener implements MapChangeListener<String, String> {

        private int ticks = 0;

        @Override
        public void onChanged(Change<? extends String, ? extends String> change) {
            ticks++;
        }
    }

    private class CollectionListener implements MapChangeListener<String, Collection<String>> {

        private int ticks = 0;

        @Override
        public void onChanged(MapChangeListener.Change<? extends String, ? extends Collection<String>> change) {
            ticks++;
        }
    }
}
