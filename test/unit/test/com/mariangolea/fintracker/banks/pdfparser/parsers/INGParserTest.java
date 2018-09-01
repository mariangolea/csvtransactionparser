package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import com.mariangolea.fintracker.banks.pdfparser.parsers.PdfPageParseResponse;

import test.com.mariangolea.fintracker.banks.pdfparser.TestUtilities;

/**
 * Tests individual methods in INGParser class.
 *
 * @author mariangolea@gmail.com
 */
public class INGParserTest extends INGParser {

	private final TestUtilities utils = new TestUtilities();
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testCompleteDateParser() {
		String input;
		Date output;
		Calendar calendar = Calendar.getInstance(INGParser.ROMANIAN_LOCALE);
		Map<String, Integer> monthNames = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG,
				INGParser.ROMANIAN_LOCALE);
		for (Entry<String, Integer> entry : monthNames.entrySet()) {
			// try to verify day, month and year based on month values.
			int year = 2000 + entry.getValue();
			int day = 1 + entry.getValue(); // Month indices start with 0, sa when constructing days add 1.
			int month = entry.getValue();
			input = day + " " + entry.getKey() + " " + year;
			output = parseCompletedDate(input);
			calendar.setTime(output);
			assertEquals("Month value not as expected.", month, calendar.get(Calendar.MONTH));
			assertEquals("Day value not as expected.", day, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals("Year value not as expected.", year, calendar.get(Calendar.YEAR));
		}

		output = parseCompletedDate("gibberish");
		assertTrue(output == null);
	}

	@Test
	public void testStartedDateParser() {
		String input = "14-08-2018";
		Date output = parseStartDate(input);
		Calendar c = Calendar.getInstance();
		c.setTime(output);
		assertEquals("Day value not as expected.", 14, c.get(Calendar.DAY_OF_MONTH));
		assertEquals("Month value not as expected.", 7, c.get(Calendar.MONTH)); // Months start from 0, so Agust is 7!
		assertEquals("Year value not as expected.", 2018, c.get(Calendar.YEAR));

		output = parseStartDate("gibberish");
		assertTrue(output == null);
	}

	@Test
	public void testAmount() {
		String input = "1.195,60";
		Float output = parseAmount(input);
		assertTrue("Amount parsing failed.", (float) 1195.6 == output);

		output = parseAmount("gibberish");
		assertTrue(output == null);
	}

	@Test
	public void testNullEmptyInput() {
		String input = null;
		PdfPageParseResponse output = parseTransactions(input);
		assertTrue("Output should be null for null input", null == output);

		input = "";
		output = parseTransactions(input);
		assertTrue("Output should be null for null input", null == output);
	}

	@Test
	public void testTransactionsHeader() {
		String badHeader = Bank.ING.relevantContentHeaderLine.substring(4);
		PdfPageParseResponse response = parseTransactions(badHeader);
		assertTrue("Output should be null for null input", null == response);

		response = parseTransactions(Bank.ING.relevantContentHeaderLine);
		// page number is set by normal callers after reposnse is created. it must
		// therefore be 0 here.
		assertTrue("Output should be null for null input", 0 == response.getPageNumber());
		assertTrue("TransactionGroups should not be null or empty",
				null != response.transactionGroups && response.transactionGroups.isEmpty());
		assertTrue("Unrecognized strings should not be null or empty",
				null != response.unrecognizedStrings && response.transactionGroups.isEmpty());
	}

	@Test
	public void testPageResponseBadInput() {
		String badHeader = Bank.ING.relevantContentHeaderLine.substring(4);
		PdfPageParseResponse response = parseTransactions(badHeader);
		assertTrue("Output should be null for null input", null == response);

		response = parseTransactions(Bank.ING.relevantContentHeaderLine);
		// page number is set by normal callers after reposnse is created. it must
		// therefore be 0 here.
		assertTrue("Output should be null for null input", 0 == response.getPageNumber());
		assertTrue("TransactionGroups should not be null or empty",
				null != response.transactionGroups && response.transactionGroups.isEmpty());
		assertTrue("Unrecognized strings should not be null or empty",
				null != response.unrecognizedStrings && response.transactionGroups.isEmpty());
	}

	@Test
	public void testPageResponseGoodSimpleInput() {
		List<String> simpleInputLines = utils.constructSimplestPositiveLinesInput(Bank.ING);
		PdfPageParseResponse response = parsePageResponse(simpleInputLines);

		// General content validation.
		assertTrue("Page response should not be null", null != response);
		assertTrue("TransactionGroups should not be null or empty",
				null != response.transactionGroups && !response.transactionGroups.isEmpty());
		assertTrue("Unrecognized strings should not be null or empty",
				null != response.unrecognizedStrings && !response.transactionGroups.isEmpty());

		// Size validations.
		assertTrue("TransactionGroups should be of size " + INGParser.OperationID.values().length,
				INGParser.OperationID.values().length == response.transactionGroups.size());
		assertTrue("Unrecognized strings should be of size " + 1, 1 == response.unrecognizedStrings.size());
	}

	@Test
	public void testSupportedTransactionsINGRoundTrip() throws IOException {
		File pdfFile = utils.writeSinglePagePDFFile(Bank.ING, folder.newFile("test.pdf"));
		assertTrue(null != pdfFile);

		PdfFileParseResponse response = new BankPDFTransactionParser().parseTransactions(pdfFile);
		assertTrue(null != response);

		// we expect a unrecognized string.
		assertTrue(!response.allOK);
		assertTrue(response.pageResponses != null && response.pageResponses.size() == 1);
		assertTrue(response.parsedTransactionGroups != null && response.parsedTransactionGroups.size() == 9);
	}
}
