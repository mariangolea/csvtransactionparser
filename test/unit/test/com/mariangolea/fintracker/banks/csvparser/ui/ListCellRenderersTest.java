package test.com.mariangolea.fintracker.banks.csvparser.ui;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.ui.categorized.tree.TransactionGroupCellRenderer;
import java.math.BigDecimal;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class ListCellRenderersTest extends FXUITest {

    @Test
    public void testBankTransactionGroupCellRenderer() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Extension one = new Extension("one");
        ListView<Extension> test = new ListView<>(FXCollections.observableArrayList(one));
        LocalExtensionGroupRenderer renderer = new LocalExtensionGroupRenderer();
        assertTrue(renderer.getText() == null);
        renderer.updateItem(one, true);
        assertTrue(renderer.getText() == null);
        renderer.updateItem(one, false);
        assertTrue(!renderer.getText().isEmpty());
    }

//    @Test
//    public void testBankTransactionGroupListSelectionRenderer() {
//        if (!fxInitialized) {
//            assertTrue("Useless in headless mode", true);
//            return;
//        }
//
//        Extension one = createExtension(new BigDecimal(100));
//        Extension two = createExtension(new BigDecimal(300));
//        Extension three = createExtension(new BigDecimal(500));
//        three.addGroup(one);
//        three.addGroup(two);
//        FilterableTreeItem<BankTransactionGroupInterface> root = new FilterableTreeItem(three);
//        FilterableTreeView test = new FilterableTreeView(root);
//        test.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//
//        Label pane = new Label();
//        TransactionGroupListSelectionListener renderer = new TransactionGroupListSelectionListener(test, pane);
//        renderer.changed(null, null, null);
//
//        // calling with no indices selected should compute total amount.
//        String text = pane.getText();
//        assertTrue(text != null && text.contains("900"));
//
//        test.getSelectionModel().selectIndices(0, 1);
//        renderer.changed(null, null, null);
//        // calling with first 2 selected should compute only for those.
//        text = pane.getText();
//        assertTrue(text != null && text.contains("400"));
//    }

    private class LocalExtensionGroupRenderer extends TransactionGroupCellRenderer {

        public LocalExtensionGroupRenderer() {
            super();
        }

        @Override
        protected void updateItem(BankTransactionGroupInterface value, boolean empty) {
            super.updateItem(value, empty); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    private Extension createExtension(final BigDecimal amount) {
        Extension one = new Extension("one");
        one.addTransaction(new BankTransaction(new Date(), new Date(), amount, BigDecimal.ZERO, "two", Arrays.asList("one", "two")));
        return one;
    }

    protected static class Extension extends BankTransactionCompanyGroup {

        public Extension(String companyDesc) {
            super(companyDesc);
        }

        @Override
        protected void addTransactions(Collection<BankTransaction> parsedTransactions) {
            super.addTransactions(parsedTransactions); 
        }

        @Override
        protected void addTransaction(BankTransaction parsedTransaction) {
            super.addTransaction(parsedTransaction); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
