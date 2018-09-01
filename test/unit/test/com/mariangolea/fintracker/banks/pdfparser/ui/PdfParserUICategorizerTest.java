/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.ui;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.DefaultTransactionCategories;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import com.mariangolea.fintracker.banks.pdfparser.ui.PdfParserUICategorizer;

import test.com.mariangolea.fintracker.banks.pdfparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class PdfParserUICategorizerTest extends PdfParserUICategorizer {

	private final TestUtilities utils = new TestUtilities();

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testSimpleDataING() throws IOException {
		// Initializes the enum values, helps unit test batch cover the momentarily dumb
		// enum.
		DefaultTransactionCategories category = DefaultTransactionCategories.CAR;

		File mockPDF = utils.writeTwoPagesPDFFile(Bank.ING, folder.newFile("testUI.pdf"));
		assertTrue(mockPDF != null);
		PdfFileParseResponse response = new BankPDFTransactionParser().parseTransactions(mockPDF);
		// tests in other files ensure response integrity, no need to do that in here.
		loadData(Arrays.asList(response));
		assertTrue(inModel != null);
		assertTrue(outModel != null);

		JMenuBar menu = createMenu();
		assertTrue(menu != null);

		final StopCondition stopCondition = new StopCondition();
		feedbackPane.getDocument().addDocumentListener(new DocumentListener() {
			int tick = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (tick == 0) {
					assertTrue(PdfParserUICategorizer.START_PARSE_MESSAGE.equals(getInstertedString(e)));
					tick++;
				} else if (tick == 1) {
					assertTrue(getInstertedString(e).startsWith(PdfParserUICategorizer.FINISHED_PARSING_PDF_FILE));
					feedbackPane.getDocument().removeDocumentListener(this);
					assertTrue(inModel != null && inModel.size() == 1);
					assertTrue(outModel != null && outModel.size() == 8);
					stopCondition.stop = true;
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// usless.
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// useless.
			}

			private String getInstertedString(DocumentEvent e) {
				String res = "";
				try {
					res = feedbackPane.getText(e.getOffset(), e.getLength());
				} catch (BadLocationException ex) {
					Logger.getLogger(PdfParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
				}

				return res;
			}
		});
		startParsingPdfFile(utils.writeSinglePagePDFFile(Bank.ING, folder.newFile("test.pdf")));

		while (!stopCondition.stop) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				Logger.getLogger(PdfParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private class StopCondition {
		private boolean stop = false;
	}
}
