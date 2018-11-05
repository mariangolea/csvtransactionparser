package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionEditPane;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class BankTransactionEditPaneTest  extends FXUITest{

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

    private class Extension extends BankTransactionEditPane {

        public Extension(UserPreferencesTestFactory factory) {
            super(factory.getUserPreferencesHandler().getPreferences());
        }

        @Override
        protected boolean isValid() {
            return super.isValid();
        }

    }
}
