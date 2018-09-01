package test.com.mariangolea.fintracker.banks.pdfparser.ui;

import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.util.Arrays;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.ui.TransactionGroupCellRenderer;
import com.mariangolea.fintracker.banks.pdfparser.ui.TransactionGroupListSelectionListener;

public class ListCellRenderersTest {

	@Test
	public void testBankTransactionGroupCellRenderer() {
		JList<BankTransactionGroup> test = new JList<>();
		BankTransactionGroup one = new BankTransactionGroup("one", "two", BankTransaction.Type.IN);
		TransactionGroupCellRenderer renderer = new TransactionGroupCellRenderer();
		Component comp = renderer.getListCellRendererComponent(test, one, 0, false, true);
		assertTrue(comp instanceof JLabel);
		JLabel label = (JLabel) comp;
		assertTrue(label.getText() != null && !label.getText().isEmpty());
		assertTrue(label.getBackground() == test.getBackground());
		assertTrue(label.getForeground() == test.getForeground());

		comp = renderer.getListCellRendererComponent(test, one, 0, true, true);
		label = (JLabel) comp;
		assertTrue(label.getBackground() == test.getSelectionBackground());
		assertTrue(label.getForeground() == test.getSelectionForeground());
	}

	@Test
	public void testBankTransactionGroupListSelectionRenderer() {
		JList<BankTransactionGroup> test = new JList<>();
		DefaultListModel<BankTransactionGroup> model = new DefaultListModel<BankTransactionGroup>();
		BankTransactionGroup one = new BankTransactionGroup("one", "two", BankTransaction.Type.IN);
		one.addTransaction(new BankTransaction("one", "two", new Date(), new Date(), 100, "two",
				BankTransaction.Type.IN, Arrays.asList("one", "two")));
		BankTransactionGroup two = new BankTransactionGroup("one", "two", BankTransaction.Type.IN);
		two.addTransaction(new BankTransaction("one", "two", new Date(), new Date(), 300, "two",
				BankTransaction.Type.IN, Arrays.asList("one", "two")));
		BankTransactionGroup three = new BankTransactionGroup("one", "two", BankTransaction.Type.IN);
		three.addTransaction(new BankTransaction("one", "two", new Date(), new Date(), 500, "two",
				BankTransaction.Type.IN, Arrays.asList("one", "two")));
		model.addElement(one);
		model.addElement(two);
		model.addElement(three);
		test.setModel(model);

		JTextPane pane = new JTextPane();
		TransactionGroupListSelectionListener renderer = new TransactionGroupListSelectionListener(test, pane);

		renderer.intervalRemoved(null);
		renderer.valueChanged(null);
		renderer.contentsChanged(null);
		// calling with no indices selected should compute total amount.
		assertTrue(pane.getText() != null && pane.getText().contains("900"));

		test.setSelectedIndices(new int[] { 0, 1 });
		renderer.intervalAdded(null);
		// calling with first 2 selected should compute only for those.
		assertTrue(pane.getText() != null && pane.getText().contains("400"));
	}
}
