package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import static com.mariangolea.fintracker.banks.csvparser.api.parser.AbstractBankParser.ROMANIAN_LOCALE;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.parser.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.BankTransactionsParser;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.ing.INGParser;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

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
    public void testAmount() {
        String input = "1.195,60";
        BigDecimal output = parseAmount(input);
        assertTrue("Amount parsing failed.", (float) 1195.6 == output.floatValue());

        output = parseAmount("gibberish");
        assertTrue(output == BigDecimal.ZERO);
    }

    @Test
    public void testSupportedTransactionsINGRoundTrip() throws IOException {
        String[] mockData = utils.constructMockCSVContentForING();
        File csvFile = utils.writeCSVFile(folder.newFile("test.csv"), mockData);
        assertTrue(null != csvFile);

        CsvFileParseResponse response = new BankTransactionsParser().parseTransactions(csvFile);
        assertTrue(null != response);

        // ING CSV files are dumber than BT ones. They end with a signature text which is irrelevant, but no way of taking it out programtically...
        assertTrue(response.unprocessedStrings.isEmpty());
        assertTrue(response.parsedTransactions != null && response.parsedTransactions.size() == 3);
    }

    @Test
    public void testMethodsBasic() {
        assertTrue(findNextTransactionLineIndex(null) == -1);
        
        List<String> impropper = new ArrayList<>();
        impropper.add("12 septembrie 2018,,,Cumparare POS,,\"\",");
        
        BankTransaction trans = parseTransaction(impropper);
        assertTrue(trans == null);
        
        impropper.clear();
        impropper.add("12 septembrie 2018,,,,,\"\",");
        trans = parseTransaction(impropper);
        assertTrue(trans == null);
        
        impropper.clear();
        impropper.add("12 kashmir 2018,,,,,\"\",");
        trans = parseTransaction(impropper);
        assertTrue(trans == null);


        impropper.clear();
        impropper.add(",,,\"\",");
        trans = parseTransaction(impropper);
        assertTrue(trans == null);

                impropper.clear();
        impropper.add("12 kashmir 2018,,,,,\"\",");
        trans = parseTransaction(impropper);
        assertTrue(trans == null);

    }

}
