package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionEditPane;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class BankTransactionEditPaneTest extends FXUITest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testConstructor() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        Extension dialog = new Extension(new UserPreferencesTestFactory());
        assertNotNull(dialog);

        assertNotNull(dialog.getEditResult());
    }

    @Test
    public void testIsValid() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        Extension dialog = new Extension(new UserPreferencesTestFactory());
        dialog.setBankTransaction(Utilities.createTransaction(new Date(), BigDecimal.ONE, BigDecimal.ZERO, "aloha"));
        assertFalse(dialog.isValid());
    }

    @Test
    public void testFieldChanges() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        Extension dialog = new Extension(factory);
        UserPreferencesInterface prefs = factory.getUserPreferencesHandler().getPreferences();
        prefs.appendDefinition("one", Arrays.asList("aloha"));
        prefs.appendDefinition("two", Arrays.asList("one"));
        prefs.resetCompanyIdentifierStrings("aloha", Arrays.asList("alo"));
        String desc = "aloha random text to get to more than 50 lines so that text splitter kicks in eventually";
        dialog.setBankTransaction(Utilities.createTransaction(new Date(), BigDecimal.ONE, BigDecimal.ZERO, desc));
        dialog.categoryPickerChanged(null);
        assertNull(dialog.getParentCategoryPickerText());
        dialog.categoryPickerChanged("one");
        assertEquals("two", dialog.getParentCategoryPickerText());

        dialog.companyDisplayNameFieldChanged("aloha");
        assertEquals("two", dialog.getParentCategoryPickerText());
        dialog.companyDisplayNameFieldChanged(null);
        prefs.resetCompanyIdentifierStrings("fake", Arrays.asList(desc));
        assertNull(dialog.getParentCategoryPickerText());
    }

    private class Extension extends BankTransactionEditPane {

        public Extension(UserPreferencesTestFactory factory) {
            super(factory.getUserPreferencesHandler().getPreferences());
        }

        @Override
        protected boolean isValid() {
            return super.isValid();
        }

        @Override
        protected void categoryPickerChanged(String newValue) {
            super.categoryPickerChanged(newValue);
        }

        @Override
        protected void companyDisplayNameFieldChanged(String newValue) {
            super.companyDisplayNameFieldChanged(newValue);
        }

        public String getParentCategoryPickerText() {
            return parentCategoryPicker.getValue();
        }

        public String getCompanyDisplayNameFieldText() {
            return companyDisplayNameField.getValue();
        }
    }
}
