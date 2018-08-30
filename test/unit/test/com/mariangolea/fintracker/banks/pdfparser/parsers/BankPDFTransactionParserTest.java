package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import java.io.File;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import static org.junit.Assert.assertEquals;
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
    public void testBasicRounTripTextSinglePage() {
        //will be written to and read from a pdf file.
        String[] mockLines = {"One", "Two"};
        File pdfFile = utils.writePDFFile(Bank.ING, mockLines);
        assertTrue(pdfFile != null);

        PDDocument pdfDoc = parser.loadPDFDocument(pdfFile);
        assertTrue(pdfDoc != null);

        String readString = parser.getText(pdfDoc, 1);
        String[] readLines = readString.split(Bank.ING.lineSeparator);
        assertTrue(areStringArraysEqual(mockLines, readLines));
    }

    @Test
    public void testBasicRounTripTextMultiplePages() {
        //will be written to and read from a pdf file.
        String[] mockLinesFirstPage = {"One", "Two"};
        String[] mockLinesSecondPage = {"Three", "Four"};
        File pdfFile = utils.writePDFFile(Bank.ING, mockLinesFirstPage, mockLinesSecondPage);
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

    private boolean areStringArraysEqual(String[] first, String[] second){
        if (first.length != second.length){
            return false;
        }
        
        for (int i=0; i < first.length; i++){
            if (!first[i].equals(second[i])){
                return false;
            }
        }
        
        return true;
    }
}
