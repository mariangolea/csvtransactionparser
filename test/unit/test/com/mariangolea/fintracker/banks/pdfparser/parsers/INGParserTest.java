package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import com.mariangolea.fintracker.banks.pdfparser.parsers.PdfPageParseResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests individual methods in INGParser class.
 *
 * @author mariangolea@gmail.com
 */
public class INGParserTest extends INGParser {

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
        String badHeader = COLUMN_NAMES_LINE.substring(4);
        PdfPageParseResponse response = parseTransactions(badHeader);
        assertTrue("Output should be null for null input", null == response);

        response = parseTransactions(COLUMN_NAMES_LINE);
        //page number is set by normal callers after reposnse is created. it must therefore be 0 here.
        assertTrue("Output should be null for null input", 0 == response.getPageNumber());
        assertTrue("TransactionGroups should not be null or empty", null != response.transactionGroups && response.transactionGroups.isEmpty());
        assertTrue("Unrecognized strings should not be null or empty", null != response.unrecognizedStrings && response.transactionGroups.isEmpty());
    }

    @Test
    public void testPageResponseBadInput() {
        String badHeader = COLUMN_NAMES_LINE.substring(4);
        PdfPageParseResponse response = parseTransactions(badHeader);
        assertTrue("Output should be null for null input", null == response);

        response = parseTransactions(COLUMN_NAMES_LINE);
        //page number is set by normal callers after reposnse is created. it must therefore be 0 here.
        assertTrue("Output should be null for null input", 0 == response.getPageNumber());
        assertTrue("TransactionGroups should not be null or empty", null != response.transactionGroups && response.transactionGroups.isEmpty());
        assertTrue("Unrecognized strings should not be null or empty", null != response.unrecognizedStrings && response.transactionGroups.isEmpty());
    }

    @Test 
    public void testPageResponseGoodSimpleInput() {
        List<String> simpleInputLines = constructSimplestPositiveLinesInput();
        PdfPageParseResponse response = parsePageResponse(simpleInputLines);
        
        //General content validation.
        assertTrue("Page response should not be null", null != response);
        assertTrue("TransactionGroups should not be null or empty", null != response.transactionGroups && !response.transactionGroups.isEmpty());
        assertTrue("Unrecognized strings should not be null or empty", null != response.unrecognizedStrings && !response.transactionGroups.isEmpty());
        
        //Size validations.
        assertTrue("TransactionGroups should be of size " + INGParser.OperationID.values().length, INGParser.OperationID.values().length == response.transactionGroups.size());
        assertTrue("Unrecognized strings should be of size " + 1, 1 == response.unrecognizedStrings.size());
    }

    private List<String> constructSimplestPositiveLinesInput() {
        //length needs to cover for correct header, all operations, and a extra useless string which has to be recognized as such.
        List<String> lines = new ArrayList<>();
        String completedDate = "18 august 2018";
        String startedDate = "16 august 2018";
        String amount = "1.230,6";
        BankTransaction.Type operationType = BankTransaction.Type.OUT;
        int extraLines = 0;
        BankTransaction transaction = null;
        String mainLine = "";
        for (OperationID operation : INGParser.OperationID.values()) {
            mainLine = amount + operation.desc + completedDate;
            switch (operation) {
                case RATE_CREDIT:
                    extraLines = 1;
                    break;
                case CASH_WITHDRAWAL:
                    extraLines = 3;
                    break;
                case ASIGURARI_GENERALE:
                    extraLines = 4;
                    break;
                case INCASARE:
                    mainLine = completedDate + operation.desc + amount;
                    operationType = BankTransaction.Type.IN;
                    extraLines = 4;
                    break;
                case COMISION:
                    extraLines = 1;
                    break;
                case PRIMA_ASIGURARE_ING:
                    extraLines = 1;
                    break;
                case PRIMA_ASIGURARE_VIATA:
                    extraLines = 0;
                    break;
                case RECALCULARI_CREDITE:
                    extraLines = 3;
                    break;
                case TRANSFER_HOMEBANK:
                    extraLines = 5;
                    break;
                default:
                    assertTrue("Unhandled operation ID found, need to update test!", false);
                    break;
            }
            lines.add(mainLine);
            for (int i = 0; i < extraLines; i++) {
                lines.add("Point less");
            }
        }

        lines.add("Pointless");
        return lines;
    }
}
