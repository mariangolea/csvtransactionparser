/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.ui;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import com.mariangolea.fintracker.banks.pdfparser.ui.PdfParserUICategorizer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.pdfparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class PdfParserUICategorizerTest extends PdfParserUICategorizer {

    private final TestUtilities utils = new TestUtilities();

    @Test
    public void testSimpleDataING() {
        BankPDFTransactionParser fac = new BankPDFTransactionParser();
        File mockPDF = utils.constructSimplestPositiveLinesInputPDFFile(Bank.ING);
        PdfFileParseResponse response = fac.parseTransactions(mockPDF);
        
        loadData(Arrays.asList(response));
        assertTrue(inModel != null);
        assertTrue(outModel != null);
    }
}
