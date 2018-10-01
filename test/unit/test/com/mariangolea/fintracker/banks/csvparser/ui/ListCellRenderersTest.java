package test.com.mariangolea.fintracker.banks.csvparser.ui;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.ui.renderer.TransactionGroupCellRenderer;
import com.mariangolea.fintracker.banks.csvparser.ui.TransactionGroupListSelectionListener;
import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ListCellRenderersTest extends FXUITest {

    @Test
    public void testBankTransactionGroupCellRenderer() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        BankTransactionCompanyGroup one = new BankTransactionCompanyGroup("one", "one", BankTransaction.Type.IN);
        ListView<BankTransactionCompanyGroup> test = new ListView<>(FXCollections.observableArrayList(one));
        LocalExtensionGroupRenderer renderer = new LocalExtensionGroupRenderer();
        assertTrue(renderer.getText() == null);
        renderer.updateItem(one, true);
        assertTrue(renderer.getText() == null);
        renderer.updateItem(one, false);
        assertTrue(!renderer.getText().isEmpty());
    }

    @Test
    public void testBankTransactionGroupListSelectionRenderer() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        } 
        
        BankTransactionCompanyGroup one = new BankTransactionCompanyGroup("one", "one", BankTransaction.Type.IN);
        one.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(100), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        BankTransactionCompanyGroup two = new BankTransactionCompanyGroup("one", "one", BankTransaction.Type.IN);
        two.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(300), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        BankTransactionCompanyGroup three = new BankTransactionCompanyGroup("one", "one", BankTransaction.Type.IN);
        three.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(500), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        ListView<BankTransactionCompanyGroup> test = new ListView<>(FXCollections.observableArrayList(one, two, three));
        test.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Label pane = new Label();
        TransactionGroupListSelectionListener renderer = new TransactionGroupListSelectionListener(test, pane);
        renderer.onChanged(null);

        // calling with no indices selected should compute total amount.
        String text = pane.getText();
        assertTrue(text != null && text.contains("900"));

        test.getSelectionModel().selectIndices(0, 1);
        renderer.onChanged(null);
        // calling with first 2 selected should compute only for those.
        text = pane.getText();
        assertTrue(text != null && text.contains("400"));
    }

    private class LocalExtensionGroupRenderer extends TransactionGroupCellRenderer {

        public LocalExtensionGroupRenderer() {
            super();
        }

        @Override
        protected void updateItem(BankTransactionCompanyGroup value, boolean empty) {
            super.updateItem(value, empty); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
