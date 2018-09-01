package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;

import test.com.mariangolea.fintracker.banks.pdfparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class BankPDFTransactionParserTest {

	private final BankPDFTransactionParser parser = new BankPDFTransactionParser();
	private final TestUtilities utils = new TestUtilities();
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testBasicRoundTripTextSinglePage() throws IOException {
		// will be written to and read from a pdf file.
		String[] mockLines = { "One", "Two" };
		File pdfFile = utils.writePDFFile(Bank.ING, folder.newFile("file.pdf"), mockLines);
		assertTrue(pdfFile != null);

		PDDocument pdfDoc = parser.loadPDFDocument(pdfFile);
		assertTrue(pdfDoc != null);

		String readString = parser.getText(pdfDoc, 1);
		String[] readLines = readString.split(Bank.ING.lineSeparator);
		assertTrue(areStringArraysEqual(mockLines, readLines));
	}

	@Test
	public void testBasicRoundTripTextMultiplePages() throws IOException {
		// will be written to and read from a pdf file.
		String[] mockLinesFirstPage = { "One", "Two" };
		String[] mockLinesSecondPage = { "Three", "Four" };
		File pdfFile = utils.writePDFFile(Bank.ING, folder.newFile("file.pdf"), mockLinesFirstPage,
				mockLinesSecondPage);
		assertTrue(pdfFile != null);

		PDDocument pdfDoc = parser.loadPDFDocument(pdfFile);
		assertTrue(pdfDoc != null);

		String readString = parser.getText(pdfDoc, 1);
		String[] readLines = readString.split(Bank.ING.lineSeparator);
		assertTrue(areStringArraysEqual(mockLinesFirstPage, readLines));

		readString = parser.getText(pdfDoc, 2);
		readLines = readString.split(Bank.ING.lineSeparator);
		assertTrue(areStringArraysEqual(mockLinesSecondPage, readLines));
	}

	private boolean areStringArraysEqual(String[] first, String[] second) {
		if (first.length != second.length) {
			return false;
		}

		for (int i = 0; i < first.length; i++) {
			if (!first[i].equals(second[i])) {
				return false;
			}
		}

		return true;
	}
}
