/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.ui;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.DefaultTransactionCategories;
import com.mariangolea.fintracker.banks.pdfparser.api.PdfFileParseResponse;
import com.mariangolea.fintracker.banks.pdfparser.parsers.BankPDFTransactionParser;
import com.mariangolea.fintracker.banks.pdfparser.ui.PdfParserUICategorizer;
import java.io.File;
import java.util.Arrays;
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
        //Initializes the enum values, helps unit test batch cover the momentarily dumb enum.
        DefaultTransactionCategories category = DefaultTransactionCategories.CAR;
        
        File mockPDF = utils.writeSinglePagePDFFile(Bank.ING);
        assertTrue(mockPDF != null);
        PdfFileParseResponse response = new BankPDFTransactionParser().parseTransactions(mockPDF);
        createUI();
        //tests in other files ensure response integrity, no need to do that in here.
        loadData(Arrays.asList(response));
        assertTrue(inModel != null);
        assertTrue(outModel != null);
    }
}
