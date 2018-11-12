package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;

public class UserPreferencesTest {

    @Test
    public void testMethods() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));
        Collection<String> expected = other.getCompanyIdentifierStrings("name");
        assertEquals(expected, Arrays.asList("company"));
        assertTrue(other.getAllCompanyIdentifierStrings().containsAll(Utilities.createList("company")));

        other.appendDefinition("two", Utilities.createList("one"));
        other.appendDefinition("three", Utilities.createList("two"));

        Collection<String> expectedList = Utilities.createList("three");
        Collection<String> topMost = other.getTopMostCategories();
        assertEquals(topMost.size(), expectedList.size());
        assertTrue(topMost.containsAll(expectedList));
    }

    @Test
    public void testDeepClone() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));

        UserPreferencesInterface copied = factory.getUserPreferencesHandler().deepCopyPreferences(other);
        assertEquals(other.getCompanyDisplayName("company"), copied.getCompanyDisplayName("company"));
    }

    @Test
    public void testApplyChanges() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));
        UserPreferencesInterface copied = factory.getUserPreferencesHandler().deepCopyPreferences(other);
        copied.editCompanyName("name", "edited");

        other.applyChanges(copied);
        assertEquals("edited", other.getCompanyDisplayName("company"));
    }

    @Test
    public void testCompanyPreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));
        other.deleteCompanyName("name");
        assertNull(other.getCompanyDisplayName("company"));

        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));
        other.resetCompanyIdentifierStrings("name", Arrays.asList("company"));
        assertNull(other.getCompanyDisplayName("name"));
        
        other.resetCompanyIdentifierStrings("name1", Arrays.asList("company"));
        assertEquals("name1", other.getCompanyDisplayName("company"));
        
        other.editCompanyIdentifier("name1", "name");
        assertNull(other.getCompanyDisplayName("name1"));
        assertEquals("name1", other.getCompanyDisplayName("company"));
    }

}
