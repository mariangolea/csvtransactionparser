package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class UserPreferencesTest {

    @Test
    public void testMethods() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.setCompanyDisplayName("company", "name");
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
        other.setCompanyDisplayName("company", "name");

        UserPreferencesInterface copied = factory.getUserPreferencesHandler().deepCopyPreferences(other);
        assertEquals(other.getCompanyDisplayName("name"), copied.getCompanyDisplayName("name"));
    }

    @Test
    public void testApplyChanges() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.setCompanyDisplayName("name", "company");
        UserPreferencesInterface copied = factory.getUserPreferencesHandler().deepCopyPreferences(other);
        copied.editCompanyName("company", "edited");

        other.applyChanges(copied);
        assertEquals("edited", other.getCompanyDisplayName("name"));
    }

    @Test
    public void testCompanyPreferences() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesInterface other = factory.getUserPreferencesHandler().getPreferences();
        other.setCompanyDisplayName("name", "company");
        other.deleteCompanyName("company");
        assertNull(other.getCompanyDisplayName("name"));

        other.setCompanyDisplayName("name", "company");
        other.deleteCompanyIdentifier("name");
        assertNull(other.getCompanyDisplayName("name"));
        
        other.setCompanyDisplayName("name", "company");
        other.resetCompanyIdentifierStrings("company", Arrays.asList("name1"));
        assertNull(other.getCompanyDisplayName("name"));
        assertEquals("company", other.getCompanyDisplayName("name1"));
        
        other.editCompanyIdentifier("name1", "name");
        assertNull(other.getCompanyDisplayName("name1"));
        assertEquals("company", other.getCompanyDisplayName("name"));
    }

}
