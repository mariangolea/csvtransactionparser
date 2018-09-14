/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.BankCsvReportsParser;
import javax.swing.JFrame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian
 */
public class BankCsvReportsParserTest {

    @Test
    public void testFrame() {
        BankCsvReportsParser parser = new BankCsvReportsParser();
        JFrame frame = parser.initFrame();
        assertTrue(frame.getTitle() != null && !frame.getTitle().isEmpty());
    }
}
