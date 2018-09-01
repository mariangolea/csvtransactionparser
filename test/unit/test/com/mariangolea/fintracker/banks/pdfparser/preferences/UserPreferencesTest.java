package test.com.mariangolea.fintracker.banks.pdfparser.preferences;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.UserDefinedTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.preferences.UserPreferences;

public class UserPreferencesTest {
	@Test
	public void testMethods() {
		UserPreferences prefs = new UserPreferences();
		prefs.addDefinition("one", new UserDefinedTransactionGroup("one"));
		boolean success = prefs.addDefinition("one", new UserDefinedTransactionGroup("one"));
		assertTrue(!success);

		success = prefs.removeDefinition("fake");
		assertTrue(!success);

		success = prefs.removeDefinition("one");
		assertTrue(success);

		success = prefs.updateDefinition("one", new UserDefinedTransactionGroup("one"));
		assertTrue(!success);

		prefs.addDefinition("one", new UserDefinedTransactionGroup("one"));
		success = prefs.updateDefinition("one", new UserDefinedTransactionGroup("one"));
		assertTrue(success);

		UserPreferences other = new UserPreferences();
		other.addDefinition("one", new UserDefinedTransactionGroup("one"));
		assertTrue(prefs.equals(other));
	}
}
