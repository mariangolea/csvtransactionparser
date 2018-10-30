package test.com.mariangolea.fintracker.banks.csvparser.preferences;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class UserPreferencesTest {

    @Test
    public void testMethods() {
        UserPreferences other = new UserPreferences();
        other.setCompanyDisplayName("company", "name");
        String expected = other.getCompanyIdentifierString("name");
        assertEquals(expected, "company");
        assertTrue(other.getCompanyIdentifierStrings().containsAll(TestUtilities.createList("company")));

        other.appendDefinition("two", TestUtilities.createList("one"));
        other.appendDefinition("three", TestUtilities.createList("two"));

        Collection<String> expectedList = TestUtilities.createList("three");
        Collection<String> topMost = other.getTopMostCategories();
        assertEquals(topMost.size(), expectedList.size()); 
        assertTrue(topMost.containsAll(expectedList));
    }
}
