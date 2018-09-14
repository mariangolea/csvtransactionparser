/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.INGParser;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

/**
 *
 * @author Marian
 */
public class INGParserTest extends INGParser{

    private final TestUtilities utils = new TestUtilities();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testCompleteDateParser() {

        Date output = parseCompletedDate("gibberish");
        assertTrue(output == null);

        output = parseCompletedDate("12 septembrie 2018");
        assertTrue(output != null);
        Calendar calendar = Calendar.getInstance(ROMANIAN_LOCALE);
        calendar.setTime(output);
        assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 12);
        assertTrue(calendar.get(Calendar.MONTH) == 8);
        assertTrue(calendar.get(Calendar.YEAR) == 2018);
    }

    @Test
    public void testStartedDateParser() {
        Date output = parseStartDate("gibberish");
        assertTrue(output == null);

        output = parseStartDate("12-08-2018");
        assertTrue(output != null);
        Calendar calendar = Calendar.getInstance(Bank.ING.locale);
        calendar.setTime(output);
        assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 12);
        assertTrue(calendar.get(Calendar.MONTH) == 7);
        assertTrue(calendar.get(Calendar.YEAR) == 2018);
    }

    @Test
    public void testAmount() {
        String input = "1.195,60";
        BigDecimal output = parseAmount(input);
        assertTrue("Amount parsing failed.", (float) 1195.6 == output.floatValue());

        output = parseAmount("gibberish");
        assertTrue(output == BigDecimal.ZERO);
    }

    @Test
    public void testSupportedTransactionsINGRoundTrip() throws IOException {
        String[] mockData = utils.constructMockCSVContentForBank(Bank.ING);
        File csvFile = utils.writeCSVFile(Bank.ING, folder.newFile("test.csv"), mockData);
        assertTrue(null != csvFile);

        CsvFileParseResponse response = new BankCSVTransactionParser().parseTransactions(csvFile);
        assertTrue(null != response);

        // ING CSV files are dumber than BT ones. They end with a signature text whic is irrelevant, but no way of taking it out programtically...
        assertTrue(response.allOK);
        assertTrue(response.parsedTransactionGroups != null && response.parsedTransactionGroups.size() == 3);
    }

    @Test
    public void testMethodsBasic() {
        assertTrue(getListOfSupportedTransactionIDs() != null);
        assertTrue(findNextTransactionLineIndex(null) == -1);
    }

}
