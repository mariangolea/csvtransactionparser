package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.UncategorizedTransactionCellRenderer;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class UncategorizedTransactionCellRendererTest extends FXUITest {

    @Test
    public void testConstructor() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        Extension rend = new Extension();

        rend.updateItem(null, true);
        assertNull(rend.getText());

        BankTransaction transaction = Utilities.createTransaction(new Date(), BigDecimal.ZERO, BigDecimal.ZERO, "aloha");
        rend.updateItem(transaction, false);
        assertEquals(rend.getText(), transaction.toString());
    }

    private class Extension extends UncategorizedTransactionCellRenderer {

        @Override
        protected void updateItem(BankTransaction item, boolean empty) {
            super.updateItem(item, empty);
        }

    }
}
