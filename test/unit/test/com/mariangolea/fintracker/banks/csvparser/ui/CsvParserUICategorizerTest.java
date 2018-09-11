/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.ui;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUICategorizer;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class CsvParserUICategorizerTest extends CsvParserUICategorizer {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testSimpleDataING() throws IOException {
        // Initializes the enum values, helps unit test batch cover the momentarily dumb
        // enum.
        File mockCSV = utils.writeCSVFile(Bank.BT, folder.newFile("testUI.csv"));
        assertTrue(mockCSV != null);
        CsvFileParseResponse response = new BankCSVTransactionParser().parseTransactions(mockCSV);
        // tests in other files ensure response integrity, no need to do that in here.
        loadData(Arrays.asList(response));
        assertTrue(inModel != null);
        assertTrue(outModel != null);

        JMenuBar menu = createMenu();
        assertTrue(menu != null);

        final StopCondition stopCondition = new StopCondition();
        feedbackPane.getDocument().addDocumentListener(new DocumentListener() {
            int tick = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (tick == 0) {
                    assertTrue(CsvParserUICategorizer.START_PARSE_MESSAGE.equals(getInstertedString(e)));
                    tick++;
                } else if (tick == 1) {
                    assertTrue(getInstertedString(e).startsWith(CsvParserUICategorizer.FINISHED_PARSING_CSV_FILE));
                    feedbackPane.getDocument().removeDocumentListener(this);
                    assertTrue(inModel != null && inModel.size() == 1);
                    assertTrue(outModel != null && outModel.size() == 8);
                    stopCondition.stop = true;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // usless.
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // useless.
            }

            private String getInstertedString(DocumentEvent e) {
                String res = "";
                try {
                    res = feedbackPane.getText(e.getOffset(), e.getLength());
                } catch (BadLocationException ex) {
                    Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
                }

                return res;
            }
        });
        startParsingCsvFile(utils.writeCSVFile(Bank.BT, folder.newFile("test.csv")));

        while (!stopCondition.stop) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class StopCondition {

        private boolean stop = false;
    }
}
