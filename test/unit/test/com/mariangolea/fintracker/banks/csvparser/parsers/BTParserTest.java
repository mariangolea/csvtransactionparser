package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.parser.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.BankTransactionsParser;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania.BTParser;
import java.math.BigDecimal;
import java.util.Locale;

import test.com.mariangolea.fintracker.banks.csvparser.Utilities;

public class BTParserTest extends BTParser {

    private final Utilities utils = new Utilities();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testCompleteDateParser() {

        Date output = parseCompletedDate("gibberish");
        assertTrue(output == null);

        output = parseCompletedDate("2018-08-12");
        assertTrue(output != null);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(output);
        assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 12);
        assertTrue(calendar.get(Calendar.MONTH) == 7);
        assertTrue(calendar.get(Calendar.YEAR) == 2018);
    }

    @Test
    public void testStartedDateParser() {
        Date output = parseStartDate("gibberish");
        assertTrue(output == null);

        output = parseStartDate("2018-08-12");
        assertTrue(output != null);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(output);
        assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 12);
        assertTrue(calendar.get(Calendar.MONTH) == 7);
        assertTrue(calendar.get(Calendar.YEAR) == 2018);
    }

    @Test
    public void testAmount() {
        String input = "1,195.60";
        BigDecimal output = parseAmount(input);
        assertTrue("Amount parsing failed.", (float) 1195.6 == output.floatValue());

        output = parseAmount("gibberish");
        assertTrue(output == BigDecimal.ZERO);
    }

    @Test
    public void testSupportedTransactionsBTRoundTrip() throws IOException {
        String[] mockData = utils.constructMockCSVContentForBT();
        File csvFile = utils.writeCSVFile(folder.newFile("test.csv"), mockData);
        assertTrue(null != csvFile);

        CsvFileParseResponse response = new BankTransactionsParser().parseTransactions(csvFile);
        assertTrue(null != response);

        // we expect a unrecognized string.
        assertTrue(!response.allCsvContentProcessed);
        assertTrue(response.foundTransactionsNumber == 3);
    }

    @Test
    public void testMethodsBasic() {
        assertTrue(findNextTransactionLineIndex(null) == 1);
    }
}
