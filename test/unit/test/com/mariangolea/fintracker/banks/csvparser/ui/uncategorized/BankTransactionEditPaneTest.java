package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.BankTransactionEditDialog;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.BankTransactionEditPane;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class BankTransactionEditPaneTest extends FXUITest {

    @Test
    public void testConstructor() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        Extension dialog = new Extension();
        assertNotNull(dialog);
    }

    @Test
    public void testIsValid() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        Extension dialog = new Extension();
        dialog.setBankTransaction(TestUtilities.createTransaction(new Date(), BigDecimal.ONE, BigDecimal.ZERO, "aloha"));
        assertFalse(dialog.isValid());
    }

    private class Extension extends BankTransactionEditPane{

        @Override
        protected boolean isValid() {
            return super.isValid();
        }
        
    }
}
