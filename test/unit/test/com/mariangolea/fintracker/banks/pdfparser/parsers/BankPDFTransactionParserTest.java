package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import java.io.File;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.pdfparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class BankPDFTransactionParserTest {

    private final BankPDFTransactionParser parser = new BankPDFTransactionParser();
    private final TestUtilities utils = new TestUtilities();

    @Test
    public void testSimpleDataING() {
        BankPDFTransactionParser fac = new BankPDFTransactionParser();
        File mockPDF = utils.constructSimplestPositiveLinesInputPDFFile(Bank.ING);
        PdfFileParseResponse response = fac.parseTransactions(mockPDF);

        assertTrue(response != null);
    }
}
