package test.com.mariangolea.fintracker.banks.csvparser.ui;

import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.util.Arrays;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.ui.TransactionGroupCellRenderer;
import com.mariangolea.fintracker.banks.csvparser.ui.TransactionGroupListSelectionListener;
import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ListCellRenderersTest {

    @Test
    public void testBankTransactionGroupCellRenderer() {
        BankTransactionDefaultGroup one = new BankTransactionDefaultGroup("one", BankTransaction.Type.IN);
        ListView<BankTransactionDefaultGroup> test = new ListView<>(FXCollections.observableArrayList(one));
        ListCell<BankTransactionDefaultGroup> cell = test.getCellFactory().call(test);
        assertTrue(cell.getText() != null && !cell.getText().isEmpty());
    }

    @Test
    public void testBankTransactionGroupListSelectionRenderer() {
        BankTransactionAbstractGroup one = new BankTransactionDefaultGroup("one",  BankTransaction.Type.IN);
        one.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(100), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        BankTransactionAbstractGroup two = new BankTransactionDefaultGroup("one", BankTransaction.Type.IN);
        two.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(300), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        BankTransactionAbstractGroup three = new BankTransactionDefaultGroup("one", BankTransaction.Type.IN);
        three.addTransaction(new BankTransaction(true, true, "one", new Date(), new Date(), new BigDecimal(500), "two",
                BankTransaction.Type.IN, Arrays.asList("one", "two")));
        ListView<BankTransactionAbstractGroup> test = new ListView<>(FXCollections.observableArrayList(one, two, three));

        Label pane = new Label();
        TransactionGroupListSelectionListener renderer = new TransactionGroupListSelectionListener(test, pane);

        // calling with no indices selected should compute total amount.
        assertTrue(pane.getText() != null && pane.getText().contains("900"));

        test.getSelectionModel().selectIndices(0, 1);
        // calling with first 2 selected should compute only for those.
        assertTrue(pane.getText() != null && pane.getText().contains("400"));
    }
}
